package csw.ml

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait BaseConfig {
	val sparkConf = new SparkConf().setMaster("local[4]").setAppName("mllib")
	val spark = SparkSession.builder().config(sparkConf).getOrCreate()
}

package object pipelines extends BaseConfig
package object features extends BaseConfig
package object fpm extends BaseConfig
package object classification extends BaseConfig
package object clustering extends BaseConfig
package object recommendation extends BaseConfig
package object validation extends BaseConfig
 