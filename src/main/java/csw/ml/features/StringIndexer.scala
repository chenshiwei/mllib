package csw.ml.features

object StringIndexer extends App {
	import org.apache.spark.ml.feature.StringIndexer

	val df = spark.createDataFrame(
		Seq((0, "a"), (1, "b"), (2, "c"), (3, "e"), (4, "a"), (5, "c"))).toDF("id", "category")

	val indexer = new StringIndexer()
		.setInputCol("category")
		.setOutputCol("categoryIndex")

//  	.setStringOrderType("frequencyAsc")

	val indexed = indexer.fit(df).transform(df)
	indexed.show()
}