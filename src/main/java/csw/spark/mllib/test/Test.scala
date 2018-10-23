package csw.ml.pipelines

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import scala.reflect.api.materializeTypeTag

case class Employee(id: Int, name: String)

// 创建DataFrame
object Test extends App {

	val conf = new SparkConf().setAppName("sql").setMaster("local")
	val sc = new SparkContext(conf)
	val sqlContext = new SQLContext(sc)

	val listOfEmployees = List(Employee(1, " huangmeiling  "), Employee(2, " sunbow  "), Employee(3, " json  "))

	val empFrame = sqlContext.createDataFrame(listOfEmployees)

	empFrame.show()

	// 聚合操作

	val aa1 = empFrame.groupBy().agg(max(empFrame("name")), avg(empFrame("id")))
	aa1.show()

	// 字符串操作

	val df2 = empFrame.select(empFrame("id"), trim(empFrame("name")))
	df2.show
	val df3 = empFrame.select(empFrame("id"), split(empFrame("name"), "o"))
	df3.show()

	// 其它操作

	val bb = empFrame.select(cos(empFrame("id")), empFrame("id"), empFrame("id") + 10, empFrame("name").substr(0, 3))

	val cc = empFrame.select(max(empFrame("id")), max(empFrame("name")))

}