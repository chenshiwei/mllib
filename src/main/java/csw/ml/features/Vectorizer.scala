package csw.ml.features

object Vectorizer extends App {
	import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}

	val df = spark.createDataFrame(Seq(
		(0, Array("a", "b", "c")),
		(1, Array("a", "b", "b", "c", "a")))).toDF("id", "words")

	// fit a CountVectorizerModel from the corpus
	val cvModel: CountVectorizerModel = new CountVectorizer()
		.setInputCol("words")
		.setOutputCol("features")
		.setVocabSize(3)
		.setMinDF(2)
		.fit(df)

	// alternatively, define CountVectorizerModel with a-priori vocabulary
	val cvm = new CountVectorizerModel(Array("a", "b", "c"))
		.setInputCol("words")
		.setOutputCol("features")

	cvModel.transform(df).show(false)

}