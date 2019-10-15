package org.apache.spark.ml.my

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkConf
import org.apache.spark.ml.param.{Param, ParamMap, Params}
import org.apache.spark.ml.util._
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
  * 作用:
  *
  * @author chensw
  * @since 2018/9/21
  */

trait MyEstimatorParams extends Params {
    final val inputCol = new Param[String](this, "inputCol", "The input column")
    final val outputCol = new Param[String](this, "outputCol", "The output column")

    def setInputCol(value: String): MyEstimatorParams.this.type = set(inputCol, value)

    def setOutputCol(value: String): MyEstimatorParams.this.type = set(outputCol, value)
}


class MyEstimatorModel(override val uid: String, val data_df: DataFrame) extends Model[MyEstimatorModel] with MyEstimatorParams with MLWritable {
    override def copy(extra: ParamMap): MyEstimatorModel = {
        val copied = new MyEstimatorModel(uid, data_df)
        copyValues(copied, extra).setParent(parent)
    }

    override def setInputCol(value: String): MyEstimatorModel.this.type = set(inputCol, value)

    override def setOutputCol(value: String): MyEstimatorModel.this.type = set(outputCol, value)

    override def transformSchema(schema: StructType): StructType = {
        val idx = schema.fieldIndex($(inputCol))
        val field = schema.fields(idx)
        if (field.dataType != DoubleType) {
            throw new Exception(s"Input type ${field.dataType} did not match input type DoubleType")
        }
        schema.add(StructField($(outputCol), DoubleType, nullable = false))
    }

    override def transform(dataset: Dataset[_]): DataFrame = {
        val avg = data_df.collect()(0).apply(0).toString.toDouble
        val std = data_df.collect()(0).apply(1).toString.toDouble
        val func = udf { label: Double => {
            if (math.abs(label - avg) > 3 * std) {
                1
            } else {
                0
            }
        }
        }
        dataset.select(col("*"), func(dataset($(inputCol))).as($(outputCol)))
    }

    override def write: MLWriter = new MyEstimatorWriter(this)

    private[MyEstimatorModel] class MyEstimatorWriter(instance: MyEstimatorModel) extends MLWriter {
        override protected def saveImpl(path: String): Unit = {
            DefaultParamsWriter.saveMetadata(instance, path, sc)
            val dataPath = new Path(path, "data").toString
            instance.data_df.repartition(1).write.parquet(dataPath)
        }
    }

}

class MyEstimator(override val uid: String) extends Estimator[MyEstimatorModel] with MyEstimatorParams {
    def this() = this(Identifiable.randomUID("MyEstimatorParams"))

    override def copy(extra: ParamMap): MyEstimator = {
        defaultCopy(extra)
    }

    override def fit(dataset: Dataset[_]): MyEstimatorModel = {
        val data_df = dataset.select(avg($(inputCol)).as("avg"), sqrt(var_pop($(inputCol))).as("std"))
        val c = new MyEstimatorModel(uid, data_df)
        c.setInputCol($(inputCol)).setOutputCol($(outputCol))
    }

    override def transformSchema(schema: StructType): StructType = {
        val idx = schema.fieldIndex($(inputCol))
        val field = schema.fields(idx)
        if (field.dataType != DoubleType) {
            throw new Exception(s"Input type ${field.dataType} did not match input type DoubleType")
        }
        schema.add(StructField($(outputCol), DoubleType, nullable = false))
    }
}

object MyEstimatorModel extends MLReadable[MyEstimatorModel] {

    override def read: MLReader[MyEstimatorModel] = new MyEstimatorReader

    override def load(path: String): MyEstimatorModel = super.load(path)

    private class MyEstimatorReader extends MLReader[MyEstimatorModel] {
        private val className = classOf[MyEstimatorModel].getName

        override def load(path: String): MyEstimatorModel = {
            val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
            val dataPath = new Path(path, "data").toString
            val data_df = sqlContext.read.parquet(dataPath)
            val model = new MyEstimatorModel(metadata.uid, data_df)
            DefaultParamsReader.getAndSetParams(model, metadata)
            model
        }
    }
}

object MyEstimator {

    //just for test
    def main(args: Array[String]): Unit = {
        val sparkConf = new SparkConf().setMaster("local[4]").setAppName("MyEstimator")
        val spark = SparkSession.builder().config(sparkConf).getOrCreate()
        val dataFrame = spark.createDataFrame(Seq(
            ("mike", 166.0), ("tom", 175.0), ("wade", 163.0), ("bad", 660.0),
            ("james", 160.0), ("black", 166.0), ("angel", 162.0), ("Emma", 177.0),
            ("weekn", 174.0), ("kelly", 166.0), ("grey", 140.0))).toDF("name", "height")
        val est = new MyEstimator
        est.setInputCol("height").setOutputCol("Abnormal")
        val model = est.fit(dataFrame)
        model.write.overwrite().save("model")
        val r = model.transform(dataFrame)
        r.show
        val model2 = MyEstimatorModel.load("model")
        model2.transform(dataFrame).show()
    }
}
