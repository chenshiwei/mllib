package csw.ml.features

import org.apache.spark.ml.linalg.Vectors

object OneHotEncoderEstimator extends App {
  import org.apache.spark.ml.feature.OneHotEncoderEstimator

  val df = spark.createDataFrame(Seq(
    (0.0, 1.0),
    (1.0, 0.0),
    (2.0, 1.0),
    (0.0, 2.0),
    (0.0, 1.0),
    (2.0, 3.0)
  )).toDF("categoryIndex1", "categoryIndex2")

  val encoder = new OneHotEncoderEstimator()
    .setInputCols(Array("categoryIndex1", "categoryIndex2"))
    .setOutputCols(Array("categoryVec1", "categoryVec2"))
    .setDropLast(false)
  val model = encoder.fit(df)

  val encoded = model.transform(df)
  encoded.show()

  val v1 = Vectors.sparse(3, Array(1), Array(1.0))
  v1.toArray.foreach(println)
}