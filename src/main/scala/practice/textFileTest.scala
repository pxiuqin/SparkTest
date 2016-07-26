package practice


/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
  * Created by lxq on 6/23/16.
  */

object TextFileTest {
  def test() {
    val file="src/main/resources/user.avsc"
    val conf= new SparkConf().setMaster("local").setAppName("TextFileTest")
    val sc=new SparkContext(conf)
    val data=sc.textFile(file,2).cache()
    val numA=data.filter(l=>l.contains("a")).count()
    val numB=data.filter(l=>l.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numA,numB))
  }
}
