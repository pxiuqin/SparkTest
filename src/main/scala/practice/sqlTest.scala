package practice

import org.apache.spark._
import org.apache.spark.sql.SQLContext

/**
  * Created by lxq on 7/10/16.
  */
object SqlTest {

  val conf = new SparkConf().setMaster("local").setAppName("SqlTest")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)

  def test() {

    import sqlContext.implicits._

    val people = sc.textFile("src/main/resources/people.txt").map(_.split(","))
      .map(p => Person(p(0), p(1).trim.toInt)).toDF()
    people.registerTempTable("people")

    //select info
    val teenager = sqlContext.sql("SELECT name,age FROM people WHERE age>=13 AND age<=19")

    teenager.map(t => "Name: " + t(0)).foreach(println)
    println("----------")
    teenager.map(t => "Name: " + t(0)).collect().foreach(println)

    teenager.map(t => "Name: " + t.getAs[String]("name")).collect().foreach(println)

    teenager.map(_.getValuesMap[Any](List("name", "age"))).collect().foreach(println)
  }

  def dataFrameOperation() = {
    val df = sqlContext.read.json("src/main/resources/people.json")
    df.show() //show data for table

    df.printSchema() //show schema
    val hao=df("name")
    print(hao.getClass())
    df.select("name").show()

    df.select(df("name"), df("age") + 1).show()

    df.filter(df("age") > 21).show()

    df.groupBy("age").count().show()

    println(df("name"))
  }

  def dataSet() = {
    import sqlContext.implicits._
    val ds = Seq(1, 2, 3).toDS()
    ds.map(_ + 1).collect()

    val ds1 = Seq(Person("xiuqin", 27)).toDS()
    ds1.show()

    val people = sqlContext.read.json("src/main/resources/people.json").as[Person]
    people.show()
  }

  def schemaTest() = {
    val people = sc.textFile("src/main/resources/people.txt")

    val schemaString = "name age"

    import org.apache.spark.sql.Row;
    import org.apache.spark.sql.types.{StructType, StructField, StringType}

    val schema = StructType(schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, true)))

    val rowRDD = people.map(_.split(",")).map(p => Row(p(0), p(1).trim))

    val peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    peopleDataFrame.registerTempTable("people")


    val results = sqlContext.sql("SELECT name FROM people")

    results.map(t => "Name: " + t(0)).collect().foreach(println)
  }

  def loadOrSaveTest() ={
    val df=sqlContext.read.load("src/main/resources/users.parquet")
    df.select("name", "favorite_color").write.save("namesAndFavColors.parquet")
  }

  def readJsonTest()={
    val df=sqlContext.read.format("json").load("src/main/resources/people.json")
    df.select("name","age").write.format("parquet").save("people.parquet")
  }

  def runSQL4fileTest()={
    val df=sqlContext.sql("SELECT * FROM parquet.`src/main/resources/users.parquet`")
    df.select("name").show()
  }
}


case class Person(name: String, age: Long)

//这里如果age：Int  dataSet测试会出错
