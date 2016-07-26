name := "SparkTest"

version := "1.0"

scalaVersion := "2.10.5"

val SparkVersion = "1.6.0"

def genSparkDeps(sparkVersion: String, pkgScope: String = "provided") = Seq(
  ("org.apache.spark" % "spark-core_2.10" % sparkVersion % pkgScope)
    .exclude("org.slf4j", "slf4j-log4j12")
    .excludeAll(
      ExclusionRule(organization = "com.apache.hadoop")
    ),
  "org.apache.spark" % "spark-mllib_2.10" % sparkVersion % pkgScope,
  "org.apache.spark" % "spark-catalyst_2.10" % sparkVersion % pkgScope,
  "org.apache.spark" % "spark-sql_2.10" % sparkVersion % pkgScope,
  "org.apache.spark" % "spark-hive_2.10" % sparkVersion % pkgScope,
  "org.apache.spark" % "spark-graphx_2.10" % sparkVersion % pkgScope
)

//libraryDependencies ++= genSparkDeps(SparkVersion)
//libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.1"