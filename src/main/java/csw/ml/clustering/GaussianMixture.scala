package csw.ml.clustering

object GaussianMixture extends App {
	import org.apache.spark.ml.clustering.GaussianMixture

	// Loads data
	val dataset = spark.read.format("libsvm").load("../mllib/src/main/resources/mllib/sample_kmeans_data.txt")

	// Trains Gaussian Mixture Model
	val gmm = new GaussianMixture()
		.setK(4)

	val model = gmm.fit(dataset)

	// output parameters of mixture model model
	for (i <- 0 until model.getK) {
		println(s"Gaussian $i:\nweight=${model.weights(i)}\n" +
			s"mu=${model.gaussians(i).mean}\nsigma=\n${model.gaussians(i).cov}\n")
	}
	model.transform(dataset).show(false)
}