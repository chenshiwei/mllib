package csw.ml.pipelines

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
  * 作用:
  *
  * @author chensw
  * @since 2018/9/21
  */
class MyTransformer(override val uid: String) extends Transformer with DefaultParamsWritable {

  final val inputCol = new Param[String](this, "inputCol", "The input column")
  final val outputCol = new Param[String](this, "outputCol", "The output column")

  def setInputCol(value: String): this.type = set(inputCol, value)

  def setOutputCol(value: String): this.type = set(outputCol, value)

  def this() = this(Identifiable.randomUID("MyTransformer "))

  def copy(extra: ParamMap): MyTransformer = {
    defaultCopy(extra)
  }

  override def transform(dataset: Dataset[_]): DataFrame = {
    val outputSchema = transformSchema(dataset.schema)
    val t = udf { score: Double => if (score >= 60) 1.0 else 0.0 }
    val metadata = outputSchema($(outputCol)).metadata
    dataset.select(col("*"), t(col($(inputCol))).as($(outputCol), metadata))
  }

  override def transformSchema(schema: StructType): StructType = {
    if (schema.fieldNames.contains($(outputCol)))
      throw new IllegalArgumentException(s"Output column ${$(outputCol)} already exists.")
    schema.add(StructField($(outputCol), DoubleType, false))
  }
}

object MyTransformer extends DefaultParamsReadable[MyTransformer] {
  override def load(path: String): MyTransformer = super.load(path)

  //just for test
  def main(args: Array[String]): Unit = {
    val dataset = spark.createDataFrame(
      Seq(("mike", 72), ("tom", 82), ("wade", 59), ("csw", 100))).toDF("name", "score")
    val myTransformer = new MyTransformer
    myTransformer.setInputCol("score").setOutputCol("pass")
    myTransformer.transform(dataset).show()
  }
}
