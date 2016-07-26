package practice

import org.apache.spark.{SparkConf, SparkContext}

object BroadcastTest {
  def test() {

    val blockSize = "4096"
    val conf = new SparkConf().setMaster("local").setAppName("BroadcastTest").set("spark.broadcast.blocksize", blockSize)
    val sc=new SparkContext(conf)

    val slices=2
    val num=1000000
    val arr1=(0 until num).toArray

    for(i<-0 until 3){
      println("Ieration "+i)
      println("===================")
      val startTime=System.nanoTime()  //纳秒
      val barr1=sc.broadcast(arr1)
      val observedSizes=sc.parallelize(1 to 10, slices).map(_=>barr1.value.length)

      //Collect the small RDD so wee can print the observed sizes locally
      observedSizes.collect().foreach(i=>println(i))
      println("Iteration %d tok %.0f milliseconds ".format(i,(System.nanoTime()-startTime)/1E6))
    }

    sc.stop()
  }
}
