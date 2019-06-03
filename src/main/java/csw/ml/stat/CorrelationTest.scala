package csw.ml.stat

/**
  * 作用:
  *
  * @author chensw
  * @since 2019/5/24
  */
object CorrelationTest extends App{
  import org.apache.spark.ml.linalg.{Matrix, Vectors}
  import org.apache.spark.ml.stat.Correlation
  import org.apache.spark.sql.Row

  val data = Seq(
    Vectors.dense(4.0,  30),
    Vectors.dense(6.0, 70),
    Vectors.dense( 8.0, 80),
    Vectors.dense(7.0,  80),
    Vectors.dense(6.0, 70)
  )

  import spark.implicits._
  val df = data.map(Tuple1.apply).toDF("features")
  df.show()
  val Row(coeff1: Matrix) = Correlation.corr(df, "features").head
  println(s"Pearson correlation matrix:\n ${coeff1(0,1)}")
  Correlation.corr(df, "features").show(false)
  val Row(coeff2: Matrix) = Correlation.corr(df, "features", "spearman").head
  println(s"Spearman correlation matrix:\n $coeff2")
}
