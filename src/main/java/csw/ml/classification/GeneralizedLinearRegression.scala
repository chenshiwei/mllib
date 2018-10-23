package csw.ml.classification

object GeneralizedLinearRegression extends App {
	import org.apache.spark.ml.regression.GeneralizedLinearRegression

	// Load training data
	val dataset = spark.read.format("libsvm")
		.load("../mllib/src/main/resources/mllib/sample_linear_regression_data.txt")

	val glr = new GeneralizedLinearRegression()

//		.setFamily("gaussian")
		.setLink("identity")
//		.setMaxIter(10)
//		.setRegParam(0.3)

	// Fit the model
	val model = glr.fit(dataset)

	// Print the coefficients and intercept for generalized linear regression model
	println(s"Coefficients: ${model.coefficients}")
	println(s"Intercept: ${model.intercept}")

	// Summarize the model over the training set and print out some metrics
	val summary = model.summary
	println(s"Coefficient Standard Errors: ${summary.coefficientStandardErrors.mkString(",")}")
	println(s"T Values: ${summary.tValues.mkString(",")}")
	println(s"P Values: ${summary.pValues.mkString(",")}")
	println(s"Dispersion: ${summary.dispersion}")
	println(s"Null Deviance: ${summary.nullDeviance}")
	println(s"Residual Degree Of Freedom Null: ${summary.residualDegreeOfFreedomNull}")
	println(s"Deviance: ${summary.deviance}")
	println(s"Residual Degree Of Freedom: ${summary.residualDegreeOfFreedom}")
	println(s"AIC: ${summary.aic}")
	println("Deviance Residuals: ")
	summary.residuals().show()
}