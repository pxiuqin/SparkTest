package practice

import org.apache.spark._
import org.apache.spark.sql.SQLContext

/**
  * Created by lxq on 7/18/16.
  */
object JsonTest {
  val conf=new SparkConf().setMaster("local").setAppName("Json Test")
  val sc=new SparkContext(conf)
  val sqlContext=new SQLContext(sc)

  def test()={
    val path="src/main/resources/people.json"
    val people=sqlContext.read.json(path)

    people.printSchema()

    people.registerTempTable("people")

    val teenagers=sqlContext.sql("SELECT name FROM people WHERE age>=13 AND age<=19")
    val antoherPeopleRDD=sc.parallelize(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil
    )

    val anotherPeople=sqlContext.read.json(antoherPeopleRDD)
    anotherPeople.printSchema()
  }
}
