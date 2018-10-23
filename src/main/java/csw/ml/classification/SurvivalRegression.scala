package csw.ml.classification

object SurvivalRegression extends App {
	import org.apache.spark.ml.linalg.Vectors
	import org.apache.spark.ml.regression.AFTSurvivalRegression

	val training = spark.createDataFrame(Seq(
		(1.218, 1.0, Vectors.dense(1.560, -0.605)),
		(2.949, 0.0, Vectors.dense(0.346, 2.158)),
		(3.627, 0.0, Vectors.dense(1.380, 0.231)),
		(0.273, 1.0, Vectors.dense(0.520, 1.151)),
		(4.199, 0.0, Vectors.dense(0.795, -0.226)))).toDF("label", "censor", "features")
	val quantileProbabilities = Array(0.3, 0.6)
	val aft = new AFTSurvivalRegression()
		.setQuantileProbabilities(quantileProbabilities)
		.setQuantilesCol("quantiles")

	val model = aft.fit(training)

	// Print the coefficients, intercept and scale parameter for AFT survival regression
	println(s"Coefficients: ${model.coefficients}")
	println(s"Intercept: ${model.intercept}")
	println(s"Scale: ${model.scale}")
	model.transform(training).show(false)
}