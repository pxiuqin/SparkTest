package practice

import org.apache.spark._

/**
  * Created by lxq on 7/15/16.
  */
object RddTest {
  val conf = new SparkConf().setMaster("local").setAppName("RDD test")
  val sc = new SparkContext(conf)

  def flatMapTest() = {
    val rdd = sc.makeRDD(1 to 6, 1)
    val mapRDD = rdd.map(x => x.toLong)
    mapRDD.collect()

    val flatMapRDD = rdd.flatMap(x => (1 to x))
    flatMapRDD.collect()

    val distinctRDD = flatMapRDD.distinct()
    distinctRDD.collect()
  }

  def repartitionTest() = {
    val rdd = sc.makeRDD(1 to 10, 1000)
    val repartitionRDD = rdd.repartition(4)
    repartitionRDD.collect()

    val coalesceRDD = rdd.coalesce(3)
    coalesceRDD.collect()
  }

  def randomSplitTest() = {
    val rdd = sc.makeRDD(1 to 10, 3)
    rdd.collect()

    //将RDD中每一个分区中类型为T的元素转换成数组Array[T]
    //这样一个分区只有一个数组元素了
    val glomRDD = rdd.glom()
    glomRDD.collect()

    val splitRDD = rdd.randomSplit(Array(1.0, 3.0, 4, 0))
    splitRDD(0).collect()
    splitRDD(1).collect()
    splitRDD(2).collect()
  }

  def unionTest() = {
    val rdd1 = sc.makeRDD(1 to 3, 1)
    val rdd2 = sc.makeRDD(2 to 4, 1)

    val unionRDD = rdd1.union(rdd2)
    unionRDD.collect()

    val intersectionRDD = rdd1.intersection(rdd2)
    intersectionRDD.collect()

    val substractRDD = rdd1.subtract(rdd2)
    substractRDD.collect()
  }

  //将RDD中的各个分区可以共享同一个对象以便提高性能
  def mapPartitionsTest() = {
    val rdd = sc.makeRDD(1 to 5, 2)
    val mapRDD = rdd.map(x => (x, x))
    val groupRDD = mapRDD.groupByKey(3)

    //val mapPartitionsRDD=groupRDD.mapPartitions(iter=>iter.filter(_.1>3))
  }

  def zipTest() = {
    val rdd = sc.makeRDD(1 to 5, 2)
    val mapRDD = rdd.map(x => (x + 1.0))

    val zipRDD = rdd.zip(mapRDD)
    zipRDD.collect()

    val rdd1 = sc.makeRDD(Array("1", "2", "3", "4", "5", "6"), 2)

    val zipPartionsRDD = rdd.zipPartitions(rdd1)((i: Iterator[Int], s: Iterator[String])
    => {
      Iterator(i.toArray.size, s.toArray.size)
    })

    zipPartionsRDD.collect()
  }

  def zipWithIndexTest() = {
    val rdd = sc.makeRDD(1 to 6, 2)
    val zipWithIndex = rdd.zipWithIndex()
    zipWithIndex.collect()

    //1,1+n,1+2*n
    val zipWithUniqueId = rdd.zipWithUniqueId()
    zipWithUniqueId.collect() 
  }
}
