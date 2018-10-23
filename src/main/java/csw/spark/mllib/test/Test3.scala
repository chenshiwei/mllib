package csw.spark.mllib.test

import java.util.Random
import scala.collection.mutable.ArrayBuffer
import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix

// 创建DataFrame
object Test3 extends App {
  import scala.collection.mutable
  val map: mutable.Map[(Int, Double, Double), List[(Long, Long, Long)]] = mutable.Map()
  def addMap(one: (Int, Double, Double, Long, Long, Long)): Unit = {
    if (map.contains(one._1, one._2, one._3) && map(one._1, one._2, one._3) != List((0, 0, 0))) {
      map.+=((one._1, one._2, one._3) -> (map(one._1, one._2, one._3) ++ List((one._4, one._5, one._6))))
    } else {
      map.+=((one._1, one._2, one._3) -> List((one._4, one._5, one._6)))
    }
  }
      0 until 7 foreach (i => 0 until 3 foreach (j => 0 until 8 foreach (k => addMap((i, j, k, 0, 0, 0)))))
  println(map)
  addMap((0, 0, 0, 0, 0, 0))
  addMap((0, 0, 0, 1, 32, 5))
      addMap((0, 0, 0, 1, 32, 5))
  println(map(58,4,964))
  println(map(0,0,0)==List((1,32,5)))
}