package csw.ml.features

object StopWordsRemover extends App {
	import org.apache.spark.ml.feature.StopWordsRemover

	val remover = new StopWordsRemover()
		.setInputCol("raw")
		.setOutputCol("filtered")


	val dataSet = spark.createDataFrame(Seq(
		(0, Seq("I", "saw", "the", "red", "balloon")),
		(1, Seq("Mary", "had", "a", "little", "lamb")))).toDF("id", "raw")

	remover.transform(dataSet).show(false)
}