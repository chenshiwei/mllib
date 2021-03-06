package csw.ml.features

import org.apache.spark.ml.feature.SQLTransformer

object Bucketizer extends App {
	import org.apache.spark.ml.feature.Bucketizer

	val splits = Array(Double.NegativeInfinity,-1000, -0.5, 0.0, 0.5,1000, Double.PositiveInfinity)

	val data = Array(-999.9, -0.5, -0.3, 0.0, 0.2, 999.9)
	val dataFrame = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

	new SQLTransformer()
	val bucketizer = new Bucketizer()
		.setInputCol("features")
		.setOutputCol("bucketedFeatures")
		.setSplits(splits)


	// Transform original data into its bucket index.
	val bucketedData = bucketizer.transform(dataFrame)

	println(s"Bucketizer output with ${bucketizer.getSplits.length - 1} buckets")
	bucketedData.show()
}