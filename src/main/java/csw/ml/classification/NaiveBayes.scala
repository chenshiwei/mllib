package csw.ml.classification

object NaiveBayes extends App {

  import org.apache.spark.ml.classification.NaiveBayes
  import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

  // Load the data stored in LIBSVM format as a DataFrame.
  val data = spark.read.format("libsvm").load("../mllib/src/main/resources/mllib/sample_libsvm_data.txt")

  // Split the data into training and test sets (30% held out for testing)
  val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)

  // Train a NaiveBayes model.
  val nb = new NaiveBayes()
  val model = nb.fit(trainingData)

  // Select example rows to display.
  val predictions = model.transform(testData)
  predictions.show()

  // Select (prediction, true label) and compute test error
  val evaluator = new MulticlassClassificationEvaluator()
    .setLabelCol("label")
    .setPredictionCol("prediction")
    .setMetricName("accuracy")
  val accuracy = evaluator.evaluate(predictions)
  println("Test set accuracy = " + accuracy)
}