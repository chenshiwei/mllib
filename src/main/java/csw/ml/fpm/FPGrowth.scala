//package csw.ml.fpm
//
//object FPGrowth extends App {
//  import org.apache.spark.ml.fpm.FPGrowth
//  import spark.implicits._
//
////  val dataset = spark.createDataset(Seq(
////    "果汁、鸡肉",
////    "鸡肉、啤酒、鸡蛋、尿布",
////    "果汁、啤酒、尿布、可乐",
////    "果汁、鸡肉、啤酒、尿布",
////    "鸡肉、果汁、啤酒、可乐")).map(t => t.split("\\、")).toDF("items")
//
//  val dataset=spark.read.load("E:/tmp/a4")
//  
//  dataset.show(false)
//  val fpgrowth = new FPGrowth().setItemsCol("items").setMinSupport(0.1).setMinConfidence(0.6)
//  val model = fpgrowth.fit(dataset)
//
//  // Display frequent itemsets.
//  model.freqItemsets.show(false)
//
//  // Display generated association rules.
//  model.associationRules.show(false)
//
//  // transform examines the input items against all the association rules and summarize the
//  // consequents as prediction
//  model.transform(dataset).show(false)
//}