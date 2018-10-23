package csw.spark.mllib.test

import org.apache.spark.mllib.clustering.{ KMeans, KMeansModel }
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.SparkContext
import java.util.UUID

object KMeansTest {
	val sc = new SparkContext("local", "ml")
	// Load and parse the data
	def main(args: Array[String]): Unit = {
		val data = sc.textFile("src/main/resources/mllib/kmeans_data.txt")
		val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

		// Cluster the data into two classes using KMeans
		val numClusters = 2
		val numIterations = 20
		val clusters = KMeans.train(parsedData, numClusters, numIterations)

		// Evaluate clustering by computing Within Set Sum of Squared Errors
		val WSSSE = clusters.computeCost(parsedData)
		println("Within Set Sum of Squared Errors = " + WSSSE)

		// Save and load model
		clusters.save(sc, s"myModelPath/${UUID.randomUUID()}")
		//		val sameModel = KMeansModel.load(sc, "myModelPath")
	}

}