package csw.ml.features

object Word2Vec extends App {
	import org.apache.spark.ml.feature.Word2Vec
	import org.apache.spark.ml.linalg.Vector
	import org.apache.spark.sql.Row

	// Input data: Each row is a bag of words from a sentence or document.
//	val documentDF = spark.createDataFrame(Seq(
//		"Hi I heard about Spark".split(" "),
//		"I wish Java could use case classes".split(" "),
//		"Logistic regression models are neat".split(" ")).map(Tuple1.apply)).toDF("text")
	spark.read.format("jdbc")
		.options(Map(
			"password" -> "DBuser123!",
			"driver" -> "com.mysql.jdbc.Driver",
			"dbtable" -> "user_behaviour",
			"user" -> "dbuser",
			"url" -> "jdbc:mysql://10.1.50.56:3306/ai"))
		.load().createOrReplaceTempView("tmp")

	val documentDF =spark.sql("select floor(timestamp/3600000)*3600000 as timestamp,ip,collect_list(command) as commands from tmp group by floor(timestamp/3600000)*3600000,ip order by timestamp")
//		.show(1000000,truncate = false)

	// Learn a mapping from words to Vectors.
	val word2Vec = new Word2Vec()
		.setInputCol("commands")
		.setOutputCol("result")
		.setVectorSize(3)
		.setMinCount(0)

	val model = word2Vec.fit(documentDF)

	val result = model.transform(documentDF)
	result.show(false)
	result.collect().foreach {
		case Row(text: Seq[_], features: Vector) =>
			println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n")
	}
}