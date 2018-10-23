package csw.spark.mllib.test

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.JdbcRDD
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import breeze.numerics._

object MysqlSparkTest {

	private val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)

	case class Blog(name: String, count: Int)

	//  def myFun(blog: JdbcRDD[Blog]): Unit = {
	//    var conn: Connection = null
	//    var ps: PreparedStatement = null
	//    val sql = "insert into blog(name, count) values (?, ?)"
	//    try {
	//      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "12345678")
	//      blog.foreach(data => {
	//        ps = conn.prepareStatement(sql)
	//        ps.setString(1, data.name)
	//        ps.setInt(2, data.count)
	//        ps.executeUpdate()
	//      })
	//    } catch {
	//      case e: Exception => LOGGER.error("", e)
	//    } finally {
	//      if (ps != null) {
	//        ps.close()
	//      }
	//      if (conn != null) {
	//        conn.close()
	//      }
	//    }
	//  }

	def main(args: Array[String]) {
		val conf = new SparkConf().setAppName("RDDToMysql").setMaster("local")
		val sc = new SparkContext(conf)
		//    Class.forName("com.mysql.jdbc.Driver").newInstance()
		//    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "12345678")

		def getConnection(): Connection = {
			Class.forName("com.mysql.jdbc.Driver").newInstance()
			DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "12345678")
		}

		val rdd = new JdbcRDD[Blog](
			sc,
			getConnection, "SELECT name, count FROM blog WHERE ID >= ? AND ID <= ?",
			2, 5, 1, r => Blog(r.getString(1), r.getInt(2)))

		//    myFun(rdd)
		rdd.foreach { println }
		//    new JdbcRDD
		val data = sc.parallelize(List(Blog("www", 10), Blog("iteblog", 20), Blog("com", 30))).cache()
		var conn: Connection = getConnection()
		var ps: PreparedStatement = null
		val sql = "insert into blog(name, count) values (?, ?)"
		data.foreach(blog => {
			ps = getConnection().prepareStatement(sql)
			ps.setString(1, blog.name)
			ps.setInt(2, blog.count)
			ps.executeUpdate()
		})
		//    data.foreachPartition(myFun)
	}
}