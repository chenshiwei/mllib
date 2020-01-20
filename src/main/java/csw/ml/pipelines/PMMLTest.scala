package csw.ml.pipelines


import java.io.File
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.ml.Transformer
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.DataFrameReader
import org.apache.spark.sql.DataFrameWriter
import org.apache.spark.sql.SQLContext
import org.jpmml.evaluator.Evaluator


/**
  * 作用:
  *
  * @author chensw
  * @since 2019/12/23
  */
object PMMLTest extends App {
    val evaluator = EvaluatorUtil.createEvaluator()

    val modelBuilder = new TransformerBuilder(evaluator).withTargetCols.withOutputCols.exploded(true)

    val transformer = modelBuilder.build
}
