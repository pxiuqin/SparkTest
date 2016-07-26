package practice

import org.apache.spark._
import org.apache.spark.sql.SQLContext

/**
  * Created by lxq on 7/26/16.
  */
object JdbcTest {
  val conf= new SparkConf().setMaster("local").setAppName("Jdbc Test")
  val sc=new SparkContext(conf)
  val sqlContext=new SQLContext(sc)

  def test={
    val jdbcDF = sqlContext.read.format("jdbc").options(
      Map("url" -> "jdbc:postgresql:dbserver",
        "dbtable" -> "schema.tablename")).load()
  }
}
