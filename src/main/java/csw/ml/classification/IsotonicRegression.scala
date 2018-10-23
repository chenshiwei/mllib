package csw.ml.classification

object IsotonicRegression extends App {
	import org.apache.spark.ml.regression.IsotonicRegression

	// Loads data.
	val dataset = spark.read.format("libsvm")
		.load("src/main/resources/mllib/sample_isotonic_regression_libsvm_data.txt")

	// Trains an isotonic regression model.
	val ir = new IsotonicRegression()
	val model = ir.fit(dataset)

	println(s"Boundaries in increasing order: ${model.boundaries}\n")
	println(s"Predictions associated with the boundaries: ${model.predictions}\n")

	// Makes predictions.
	model.transform(dataset).show()
}