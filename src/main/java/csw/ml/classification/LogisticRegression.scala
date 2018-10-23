package csw.ml.classification

object LogisticRegression extends App {
	import org.apache.spark.ml.classification.LogisticRegression

	// Load training data
	val training = spark.read.format("libsvm").load("../mllib/src/main/resources/mllib/sample_libsvm_data.txt")

	training.show(false)
	val lr = new LogisticRegression()
		.setMaxIter(10)
		.setRegParam(0.3)
		.setElasticNetParam(0.8)
//  	.setWeightCol()

	// Fit the model
	val lrModel = lr.fit(training)

	// Print the coefficients and intercept for logistic regression
	println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

	// We can also use the multinomial family for binary classification
	val mlr = new LogisticRegression()
		.setMaxIter(10)
		.setRegParam(0.3)
		.setElasticNetParam(0.8)
		.setFamily("multinomial")

	val mlrModel = mlr.fit(training)

	// Print the coefficients and intercepts for logistic regression with multinomial family
	println(s"Multinomial coefficients: ${mlrModel.coefficientMatrix}")
	println(s"Multinomial intercepts: ${mlrModel.interceptVector}")

	//	import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}
	//
	//// Extract the summary from the returned LogisticRegressionModel instance trained in the earlier
	//// example
	//val trainingSummary = lrModel.summary
	//
	//// Obtain the objective per iteration.
	//val objectiveHistory = trainingSummary.objectiveHistory
	//println("objectiveHistory:")
	//objectiveHistory.foreach(loss => println(loss))
	//
	//// Obtain the metrics useful to judge performance on test data.
	//// We cast the summary to a BinaryLogisticRegressionSummary since the problem is a
	//// binary classification problem.
	//val binarySummary = trainingSummary.asInstanceOf[BinaryLogisticRegressionSummary]
	//
	//// Obtain the receiver-operating characteristic as a dataframe and areaUnderROC.
	//val roc = binarySummary.roc
	//roc.show()
	//println(s"areaUnderROC: ${binarySummary.areaUnderROC}")
	//
	//// Set the model threshold to maximize F-Measure
	//val fMeasure = binarySummary.fMeasureByThreshold
	//val maxFMeasure = fMeasure.select(max("F-Measure")).head().getDouble(0)
	//val bestThreshold = fMeasure.where($"F-Measure" === maxFMeasure)
	//  .select("threshold").head().getDouble(0)
	//lrModel.setThreshold(bestThreshold)
}