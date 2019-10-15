package org.apache.spark.ml.my

import org.apache.spark.SparkConf
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.{DefaultParamsReadable, DefaultParamsWritable, Identifiable}
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

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
    val t = udf { score: Double => if (score > 170) 1.0 else 0.0 }
    val metadata = outputSchema($(outputCol)).metadata
    dataset.select(col("*"), t(col($(inputCol))).as($(outputCol), metadata))
  }

  override def transformSchema(schema: StructType): StructType = {
    if (schema.fieldNames.contains($(outputCol)))
      throw new IllegalArgumentException(s"Output column ${$(outputCol)} already exists.")
    schema.add(StructField($(outputCol), DoubleType, nullable = false))
  }
}

object MyTransformer extends DefaultParamsReadable[MyTransformer] {
  override def load(path: String): MyTransformer = super.load(path)

  //just for test
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[4]").setAppName("MyTransformer")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val dataFrame = spark.createDataFrame(
      Seq(("mike", 166.0), ("tom", 175.0), ("wade", 163.0), ("csw", 185.0))).toDF("name", "height")
    val myTransformer = new MyTransformer
    myTransformer.setInputCol("height").setOutputCol("h170")
    myTransformer.transform(dataFrame).show()
  }
}
