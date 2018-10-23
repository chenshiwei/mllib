package csw.ml.features

/**
  * 作用:
  *
  * @author chensw
  * @since 2018/10/17
  */
object FeatureHasher extends App {

  import org.apache.spark.ml.feature.FeatureHasher

  val dataset = spark.createDataFrame(Seq(
    (2.2, true, "1", "foo"),
    (3.3, false, "2", "bar"),
    (4.4, false, "3", "baz"),
    (5.5, false, "4", "foo")
  )).toDF("real", "bool", "stringNum", "string")

  val hasher = new FeatureHasher()
    .setInputCols("real", "bool", "stringNum", "string")
    .setOutputCol("features")

  val featurized = hasher.transform(dataset)
  featurized.show(false)

}
