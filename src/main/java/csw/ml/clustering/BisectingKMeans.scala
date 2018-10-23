package csw.ml.clustering

object BisectingKMeans extends App {
	import org.apache.spark.ml.clustering.BisectingKMeans

	// Loads data.
	val dataset = spark.read.format("libsvm").load("../mllib/src/main/resources/mllib/sample_kmeans_data.txt")

	// Trains a bisecting k-means model.
	val bkm = new BisectingKMeans().setK(2).setSeed(1)

	val model = bkm.fit(dataset)

	// Evaluate clustering.
	val cost = model.computeCost(dataset)
	println(s"Within Set Sum of Squared Errors = $cost")

	// Shows the result.
	println("Cluster Centers: ")
	val centers = model.clusterCenters
	centers.foreach(println)
}