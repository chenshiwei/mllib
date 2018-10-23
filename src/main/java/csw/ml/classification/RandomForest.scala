package csw.ml.classification

import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.feature.LabeledPoint

object RandomForest extends App {
  import org.apache.spark.ml.Pipeline
  import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
  import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
  import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}

  // Load and parse the data file, converting it to a DataFrame.
  //	val data = spark.read.format("libsvm").load("src/main/resources/mllib/sample_libsvm_data.txt")
  def getClazz(clazz: String): Int = {
    clazz match {
      case "Iris-setosa" => 0
      case "Iris-versicolor" => 1
      case "Iris-virginica" => 2
    }

  }
  import spark.implicits._
  val data = spark.read.format("csv").option("header", true).load("src/main/resources/mllib//iris.dat").rdd
    .map(row => LabeledPoint(getClazz(row.getString(4)), Vectors.dense(row.getString(0).toDouble, 
        row.getString(1).toDouble, row.getString(2).toDouble, row.getString(3).toDouble)))
    .toDF("label", "features")
//    data.show(150)
//   Index labels, adding metadata to the label column.
//   Fit on whole dataset to include all labels in index.
  	val labelIndexer = new StringIndexer()
  		.setInputCol("label")
  		.setOutputCol("indexedLabel")
  		.fit(data)
  	// Automatically identify categorical features, and index them.
  	// Set maxCategories so features with > 4 distinct values are treated as continuous.
  	val featureIndexer = new VectorIndexer()
  		.setInputCol("features")
  		.setOutputCol("indexedFeatures")
  		.setMaxCategories(3)
  		.fit(data)
  
//  	 Split the data into training and test sets (30% held out for testing).
  	val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))
  
  	// Train a RandomForest model.
  	val rf = new RandomForestClassifier()
  		.setLabelCol("indexedLabel")
  		.setFeaturesCol("indexedFeatures")
  		.setImpurity("gini")
  		.setNumTrees(2)
  
  	// Convert indexed labels back to original labels.
  	val labelConverter = new IndexToString()
  		.setInputCol("prediction")
  		.setOutputCol("predictedLabel")
  		.setLabels(labelIndexer.labels)
  
  	// Chain indexers and forest in a Pipeline.
  	val pipeline = new Pipeline()
  		.setStages(Array(labelIndexer, featureIndexer, rf, labelConverter))
  
  	// Train model. This also runs the indexers.
  	val model = pipeline.fit(trainingData)
  
  	// Make predictions.
  	val predictions = model.transform(testData)
  
  	// Select example rows to display.
  	predictions.select("predictedLabel", "label", "features").show(300,false)
  
  	// Select (prediction, true label) and compute test error.
  	val evaluator = new MulticlassClassificationEvaluator()
  		.setLabelCol("indexedLabel")
  		.setPredictionCol("prediction")
  		.setMetricName("accuracy")
  	val accuracy = evaluator.evaluate(predictions)
  	println("Test Error = " + (1.0 - accuracy))
  
  	val rfModel = model.stages(2).asInstanceOf[RandomForestClassificationModel]
  	println("Learned classification forest model:\n" + rfModel.toDebugString)
}