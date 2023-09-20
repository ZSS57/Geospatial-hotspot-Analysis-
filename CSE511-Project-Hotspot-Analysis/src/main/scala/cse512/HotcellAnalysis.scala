package cse512

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.functions._

object HotcellAnalysis {
  Logger.getLogger("org.spark_project").setLevel(Level.WARN)
  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)
  Logger.getLogger("com").setLevel(Level.WARN)

def runHotcellAnalysis(spark: SparkSession, pointPath: String): DataFrame =
{
  // Load the original data from a data source
  var pickupInfo = spark.read.format("com.databricks.spark.csv").option("delimiter",";").option("header","false").load(pointPath);
  pickupInfo.createOrReplaceTempView("nyctaxitrips")
  pickupInfo.show()

  // Assign cell coordinates based on pickup points
  spark.udf.register("CalculateX",(pickupPoint: String)=>((
    HotcellUtils.CalculateCoordinate(pickupPoint, 0)
    )))
  spark.udf.register("CalculateY",(pickupPoint: String)=>((
    HotcellUtils.CalculateCoordinate(pickupPoint, 1)
    )))
  spark.udf.register("CalculateZ",(pickupTime: String)=>((
    HotcellUtils.CalculateCoordinate(pickupTime, 2)
    )))
  pickupInfo = spark.sql("select CalculateX(nyctaxitrips._c5),CalculateY(nyctaxitrips._c5), CalculateZ(nyctaxitrips._c1) from nyctaxitrips")
  var newCoordinateName = Seq("x", "y", "z")
  pickupInfo = pickupInfo.toDF(newCoordinateName:_*)
  pickupInfo.show()

  // Define the min and max of x, y, z
  val minX = -74.50/HotcellUtils.coordinateStep
  val maxX = -73.70/HotcellUtils.coordinateStep
  val minY = 40.50/HotcellUtils.coordinateStep
  val maxY = 40.90/HotcellUtils.coordinateStep
  val minZ = 1
  val maxZ = 31
  val numCells = (maxX - minX + 1)*(maxY - minY + 1)*(maxZ - minZ + 1)

  // YOU NEED TO CHANGE THIS PART

  pickupInfo.createOrReplaceTempView("pickupInfo")

  spark.udf.register("NumOfNeighbors", (x:Int,y:Int,z:Int,minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int) => ((HotcellUtils.NumOfNeighbors(x,y,z,minX, minY, minZ, maxX, maxY, maxZ))))
  spark.udf.register("CalculateGScore",(mean: Double, s:Double, neighbors: Int, SumOfPointsNeighbor: Int, numCells:Int)=>((
    HotcellUtils.CalculateGScore(mean, s, neighbors, SumOfPointsNeighbor, numCells))))

  // get the num of all points in each cell
  var numpoints = spark.sql("select x,y,z,count(*) as totalnum from pickupInfo where x>=" + minX + " and x<=" + maxX +
    " and y>=" + minY + " and y<=" + maxY + " and z>=" + minZ + " and z<=" + maxZ+ " group by x,y,z")
  numpoints.createOrReplaceTempView("numpoints")
  numpoints.show()

  //get the sum of points and square sum
  val points = spark.sql("select sum(totalnum) as sum, sum(totalnum * totalnum) as squsum from numpoints")

  //get the mean and varience
  val mean: Double = points.first().getLong(0).toDouble/numCells.toDouble
  val s: Double = math.sqrt((points.first().getLong(1).toDouble / numCells.toDouble) - (mean.toDouble * mean.toDouble))

  //check neighbor
  val checkneighbours = spark.sql("select cella.x as x, cella.y as y, cella.z as z," +  // two ways to get neighbors
    "NumOfNeighbors(cella.x, cella.y, cella.z," + minX + "," + minY + "," + minZ + "," + maxX + "," + maxY + "," + maxZ + ") as neighbors, " +
    "sum(cellb.totalnum) as SumOfPointsNeighbor " +
    "from numpoints as cella, numpoints as cellb where " +
    "cellb.x in(cella.x-1, cella.x, cella.x+1)"+
    "and cellb.y in(cella.y-1, cella.y, cella.y+1)"+
    "and cellb.z in(cella.z-1, cella.z, cella.z+1)"+
    "group by cella.x, cella.y, cella.z")

  checkneighbours.createOrReplaceTempView("checkneighbors")

  /*
  val a = spark.sql("select neighbors, SumOfPointsNeighbor from checkneighbors where x = -7399 and y = 4075 and z = 15")
  a.createOrReplaceTempView("a")
  a.show()
  */
  // get gcore
  val getgscore =  spark.sql("select x,y,z,CalculateGScore("+ mean + ","+ s +",neighbors,SumOfPointsNeighbor," + numCells+") as gscore from checkneighbors order by gscore desc")
  getgscore.createOrReplaceTempView("getgscore")
  getgscore.show()

  val result = spark.sql("select x,y,z from getgscore")
  result.createOrReplaceTempView("result")
  //result.show()
  return result

}
}
