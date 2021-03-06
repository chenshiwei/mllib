package csw.ml.pipelines

import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator

object LogisticRegression2 extends App {

  import org.apache.spark.ml.Pipeline
  import org.apache.spark.ml.classification.LogisticRegression
  import org.apache.spark.ml.feature.{HashingTF, Tokenizer}

  // Prepare training documents from a list of (id, text, label) tuples.
  val training = spark.createDataFrame(Seq(
    (0L, "a b c d e spark", 1.0),
    (1L, "b d", 0.0),
    (2L, "spark f g h", 1.0),
    (3L, "hadoop mapreduce", 0.0)
  )).toDF("id", "text", "label")

  //  training.show()

  // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.
  val tokenizer = new Tokenizer()
    .setInputCol("text")
    .setOutputCol("words")

  tokenizer.transform(training).show(false)

  val hashingTF = new HashingTF()
    .setNumFeatures(1000)
    .setInputCol(tokenizer.getOutputCol)
    .setOutputCol("features")

  hashingTF.transform(tokenizer.transform(training)).show(false)

  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.001)

  //  val model = lr.fit(hashingTF.transform(tokenizer.transform(training)))

  val pipeline = new Pipeline()
    .setStages(Array(tokenizer, hashingTF, lr))

  //// Fit the pipeline to training documents.
  val model: PipelineModel = pipeline.fit(training)
  //
  //// Now we can optionally save the fitted pipeline to disk
  //model.write.overwrite().save("tmp/spark-logistic-regression-model")
  //  val samePipeline = Pipeline.load("/tmp/unfit-lr-model")
  //
  //// We can also save this unfit pipeline to disk
  //pipeline.write.overwrite().save("tmp/unfit-lr-model")
  //
  //// And load it back in during production
  //val sameModel = PipelineModel.load("/tmp/spark-logistic-regression-model")
  //
  //// Prepare test documents, which are unlabeled (id, text) tuples.
  val test = spark.createDataFrame(Seq(
    (4L, "spark i j k",1.0),
    (5L, "l m n",0.0),
    (6L, "spark hadoop spark",1.0),
    (7L, "apache hadoop",0.0)
  )).toDF("id", "text","label")
  //
  //// Make predictions on test documents.
  model.transform(test).show()

  val bce = new BinaryClassificationEvaluator()
  bce.setRawPredictionCol("prediction")
    .setLabelCol("label")
//    .setMetricName("accuracy")
  println(bce.evaluate(model.transform(test)))
  //  .select("id", "te
  // xt", "probability", "prediction")
  //  .collect()
  //  .foreach { case Row(id: Long, text: String, prob: Vector, prediction: Double) =>
  //    println(s"($id, $text) --> prob=$prob, prediction=$prediction")
  //  }
}