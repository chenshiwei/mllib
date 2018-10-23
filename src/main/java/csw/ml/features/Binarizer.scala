package csw.ml.features

object Binarizer extends App {
	import org.apache.spark.ml.feature.Binarizer

	val data = Array((0, 0.1), (1, 0.8), (2, 0.2))
	val dataFrame = spark.createDataFrame(data).toDF("id", "feature")

	val binarizer: Binarizer = new Binarizer()
		.setInputCol("feature")
		.setOutputCol("binarized_feature")
		.setThreshold(0.5)



	val binarizedDataFrame = binarizer.transform(dataFrame)

	println(s"Binarizer output with Threshold = ${binarizer.getThreshold}")
	binarizedDataFrame.show()
}