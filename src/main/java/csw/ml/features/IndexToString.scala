package csw.ml.features

object IndexToString extends App {
	import org.apache.spark.ml.attribute.Attribute
	import org.apache.spark.ml.feature.{ IndexToString, StringIndexer }

	val df = spark.createDataFrame(Seq(
		(0, "a"),
		(1, "b"),
		(2, "c"),
		(3, "a"),
		(4, "a"),
		(5, "c"))).toDF("id", "category")

	val indexer = new StringIndexer()
		.setInputCol("category")
		.setOutputCol("categoryIndex")
		.fit(df)
	indexer.labels.foreach(println)
	val indexed = indexer.transform(df)

	println(s"Transformed string column '${indexer.getInputCol}' " +
		s"to indexed column '${indexer.getOutputCol}'")
	indexed.show()

	val inputColSchema = indexed.schema(indexer.getOutputCol)
	println(s"StringIndexer will store labels in output column metadata: " +
		s"${Attribute.fromStructField(inputColSchema).toString}\n")

	val converter = new IndexToString()
		.setInputCol("categoryIndex")
		.setOutputCol("originalCategory")


//  	.setLabels(Array("category"))
//println("*****************************************")
//println(converter.getLabels)
//println(converter.getLabels)
	val converted = converter.transform(indexed)

	println(s"Transformed indexed column '${converter.getInputCol}' back to original string " +
		s"column '${converter.getOutputCol}' using labels in metadata")
	converted.select("id", "categoryIndex", "originalCategory").show()

//	println(converter.getLabels)
}