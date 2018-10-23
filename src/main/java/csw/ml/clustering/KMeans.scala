package csw.ml.clustering

object KMeans extends App {
	import org.apache.spark.ml.clustering.KMeans

	// Loads data.
	val dataset = spark.read.format("libsvm").load("../mllib/src/main/resources/mllib/sample_kmeans_data.txt")

	// Trains a k-means model.
	val kmeans = new KMeans().setK(2).setSeed(1L)


	val model = kmeans.fit(dataset)

	// Evaluate clustering by computing Within Set Sum of Squared Errors.
	val WSSSE = model.computeCost(dataset)
	println(s"Within Set Sum of Squared Errors = $WSSSE")

	// Shows the result.
	println("Cluster Centers: ")
	model.clusterCenters.foreach(println)
}