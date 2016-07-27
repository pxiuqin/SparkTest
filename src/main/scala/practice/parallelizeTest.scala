package practice

import org.apache.spark._

import scala.math._

/**
  * Created by lxq on 7/27/16.
  */
object ParallelizeTest {
  val conf=new SparkConf().setMaster("local").setAppName("Parallelize Test")
  val sc=new SparkContext(conf)

  def computePI()={
    val slices=20
    val n= math.min(1000000L * slices,Int.MaxValue).toInt  //防止给出的值越界
    val count=sc.parallelize(1 until n, slices).map{i=>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / n)
  }
}
