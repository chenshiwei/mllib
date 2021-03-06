package csw.wxh.mllib.c12

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{ LabeledPoint, LinearRegressionWithSGD }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.ml.classification.LogisticRegression

object irisLogicRegression {
	def main(args: Array[String]): Unit = {

	val conf = new SparkConf() //创建环境变量
		.setMaster("local") //设置本地化处理
		.setAppName("irisLogicRegression ") //设定名称
	val sc = new SparkContext(conf) //创建环境变量实例
	val data = sc.textFile("c:/a.txt") //读取数据
	val parsedData = data.map { line => //处理数据
		val parts = line.split(" ") //按空格分割
		LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).toDouble)) //固定格式
	}.cache() //加载数据
//	val model = LogisticRegression.train(parsedData, 20) //创建模型
//	val valuesAndPreds = parsedData.map { point =>
//		 //创建均方误差训练数据
//			val prediction = model.predict(point.features) //创建数据
//			(point.label, prediction) //创建预测数据
//		}
//		val MSE = valuesAndPreds.map { case (v, p) => math.pow((v - p), 2) }.mean() //计算均方误差
//		println("均方误差结果为:" + MSE) //打印结果
	}
}

