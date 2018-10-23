package csw.ml

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object Test extends App {
  val sparkConf = new SparkConf().setMaster("local[4]").setAppName("mllib")
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()
  val sc = spark.sparkContext
  import spark.implicits._

  val sb = new StringBuilder("abc")
  //  val bcsb = sc.broadcast(sb)
  sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8)).foreach(i => {
    sb.append(i)
    //    bcsb.value.append(i)
  })
  println(sb)
  //  println(bcsb.value)

  import org.apache.spark.mllib.linalg.Vectors
  import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}

  val observations = sc.parallelize(
    Seq(
      Vectors.dense(1.0, 2, 3.0),
      Vectors.dense(11.0, 3.0, 2.0),
      Vectors.dense(4.0, 3.0, 2.0),
      Vectors.dense(4.0, 4.0, 5.0)))

  // Compute column summary statistics.
  val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)
  println(summary.mean) // a dense vector containing the mean value for each column
  println(summary.variance) // column-wise variance
  println(summary.numNonzeros) // number of nonzeros in each column
}