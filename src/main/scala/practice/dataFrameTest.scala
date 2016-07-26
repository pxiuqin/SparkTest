package practice

import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

/**
  * Created by lxq on 7/15/16.
  */
object DataFrameTest {
  val conf = new SparkConf().setMaster("local").setAppName("dataFrame test")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)

  import sqlContext.implicits._

  def testParquet() = {
    val df1 = sc.makeRDD(1 to 5).map(i => (i, i * 2)).toDF("single", "double")
    df1.write.parquet("src/main/resources/test_table/key=1") //表示选择single列

    val df2 = sc.makeRDD(6 to 10).map(i => (i, i * 3)).toDF("single", "triple")
    df2.write.parquet("src/main/resources/test_table/key=2") //表示选择triple列

    val df3 = sqlContext.read.option("mergeSchema", "true").parquet("src/main/resources/test_table")
    df3.printSchema()
  }

  def testUnion() = {

    val df_a = sc.makeRDD(1 to 5).map(i => (i * 2.0, i)).toDF("double", "single")
    val df_b = sc.makeRDD(5 to 10).map(i => (i, i * 2.0)).toDF("single", "double")
    val isRedundancy = true
    val isSelectFirstDataset = true
    var mergeDataset = df_a

    if (df_a.schema.size == df_b.schema.size) {
      if (df_a.schema.forall(a => df_b.schema.exists(b => b.name == a.name && b.dataType == a.dataType))) {
        mergeDataset = df_a.unionAll(df_b.select(df_a.schema.fieldNames.map(df_b(_)).array:_*))

        if (isRedundancy) {
          mergeDataset = mergeDataset.distinct()
        }
      }
    }

    if(mergeDataset.eq(df_a)) println("equal")

    /*if (df_a.schema.size == df_b.schema.size) {
      if (df_a.schema.forall(a => df_b.schema.exists(b => b.name == a.name && b.dataType == a.dataType))) {
        val hao=df_b.select(df_a.schema.fieldNames.map(df_b(_)).array:_*)
        mergeDataset = df_a.unionAll(hao)
        //var h = ""

        //val ao = df_a.schema.fieldNames.foreach(a => h += "," + """"""+a+"""""")
        //h=h.replaceFirst(",", "")
        //println(h)
        //var a= StringToColumn(StringContext(h))
        //def selectExpr(exprs : scala.Predef.String*) ={}
        //df_b.selectExpr(df_a.schema.fieldNames:_*).show()     //使用：_* 实现String×传递
        //df_b.select(
        //df_b.selectExpr("double", "single").show()
        //df_b.toDF("double", "single").show()
        //mergeDataset = df_a.unionAll(df_b.select(df_b("double"), df_b("single")))

        if (isRedundancy) {
          mergeDataset = mergeDataset.distinct()
        }
      }
    }*/

    mergeDataset.printSchema()
    mergeDataset.show()
  }
}
