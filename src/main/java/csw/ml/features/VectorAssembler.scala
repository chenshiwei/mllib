package csw.ml.features

object VectorAssembler extends App {
	import org.apache.spark.ml.feature.VectorAssembler
	import org.apache.spark.ml.linalg.Vectors

	val dataset = spark.createDataFrame(
		Seq((0, 18, 1.0, Vectors.dense(0.0, 10.0, 0.5), 1.0))).toDF("id", "hour", "mobile", "userFeatures", "clicked")

	val assembler = new VectorAssembler()
		.setInputCols(Array("hour", "mobile", "userFeatures"))
		.setOutputCol("features")

	val output = assembler.transform(dataset)
	output.show()
	println("Assembled columns 'hour', 'mobile', 'userFeatures' to vector column 'features'")
	output.select("features", "clicked").show(false)
}