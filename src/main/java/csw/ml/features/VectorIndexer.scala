package csw.ml.features

object VectorIndexer extends App {
	import org.apache.spark.ml.feature.VectorIndexer

	val data = spark.read.format("libsvm").load("../mllib/src/main/resources/mllib/sample_libsvm_data.txt")

	val indexer = new VectorIndexer()
		.setInputCol("features")
		.setOutputCol("indexed")
		.setMaxCategories(10)

	val indexerModel = indexer.fit(data)

	val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet
	println(s"Chose ${categoricalFeatures.size} categorical features: " +
		categoricalFeatures.mkString(", "))

	// Create new column "indexed" with categorical values transformed to indices
	val indexedData = indexerModel.transform(data)
	indexedData.show(false)
}