package practice

import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
/**
  * Created by lxq on 7/12/16.
  */
object parquetTest {

  val conf=new SparkConf().setMaster("local").setAppName("parquet test")
  val sc=new SparkContext(conf)
  val sqlContext=new SQLContext(sc)

  def test()={
    import sqlContext.implicits._

    val people:RDD[Person]=sc.textFile("src/main/resources/people.txt").map(_.split(","))
      .map(p=>Person(p(0),p(1).trim.toInt))

    people.toDF().write.parquet("people.parquet")

    val parquetFile=sqlContext.read.parquet("people.parquet")

    parquetFile.registerTempTable("parquetFile")
    val teenagers=sqlContext.sql("SELECT name FROM parquetFile WHERE age>=13 AND age<=19")
    teenagers.map(t=>"Name: "+t(0)).collect().foreach(println)
  }
}
