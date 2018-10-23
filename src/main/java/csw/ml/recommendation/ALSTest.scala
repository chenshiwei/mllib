package csw.ml.recommendation


object ALSTest extends App {
//
//  val ratings = spark.read.textFile("src/main/resources/mllib/als/sample_movielens_ratings.txt")
//    .map(parseRating)
//    .toDF()
//
//  import org.apache.spark.ml.evaluation.RegressionEvaluator
//  import org.apache.spark.ml.recommendation.ALS
//  val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))
//  // Build the recommendation model using ALS on the training data
//  val als = new ALS()
//    .setMaxIter(5)
//    .setRegParam(0.01)
//    .setUserCol("userId")
//    .setItemCol("movieId")
//    .setRatingCol("rating")
//  val model = als.fit(training)
//  // Evaluate the model by computing the RMSE on the test data
//  val predictions = model.transform(test)
//  //		val als = new ALS()
//  //  .setMaxIter(5)
//  //  .setRegParam(0.01)
//  //  .setImplicitPrefs(true)
//  //  .setUserCol("userId")
//  //  .setItemCol("movieId")
//  //  .setRatingCol("rating")
//  val evaluator = new RegressionEvaluator()
//    .setMetricName("rmse")
//    .setLabelCol("rating")
//    .setPredictionCol("prediction")
//  val rmse = evaluator.evaluate(predictions)
//
//  def parseRating(str: String): Rating = {
//    val fields = str.split("::")
//    assert(fields.size == 4)
//    Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
//  }
//
//  case class Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long)
//  println(s"Root-mean-square error = $rmse")
}