package csw.spark.mllib.test

import java.util.Random
import scala.collection.mutable.ArrayBuffer
import breeze.linalg.DenseVector
import breeze.linalg.DenseMatrix

// 创建DataFrame
object Test2 extends App {

  val rand = new Random

  val v1 = DenseVector.rand(100000)
  val v2 = DenseVector.rand(100000)
  var t = System.nanoTime()
  println(v1.dot(v2))
  println(System.nanoTime() - t)
  t = System.nanoTime()
  println(v1.dot(v2))
  println(System.nanoTime() - t)
  t = System.nanoTime()
  var c = 0.0
  for (i <- 0 until v1.size) {
    c += v1(i) * v2(i)
  }
  println(c)
  println(System.nanoTime() - t)
}