/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package examples.ml

// $example on$
import org.apache.spark.ml.feature.StandardScaler
// $example off$
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object StandardScalerExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("StandardScalerExample")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    // $example on$
    val dataFrame = sqlContext.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(false)

    // Compute summary statistics by fitting the StandardScaler.
    val scalerModel = scaler.fit(dataFrame)

    // Normalize each feature to have unit standard deviation.
    val scaledData = scalerModel.transform(dataFrame)
    scaledData.show()
    // $example off$
    sc.stop()
  }
}
// scalastyle:on println
