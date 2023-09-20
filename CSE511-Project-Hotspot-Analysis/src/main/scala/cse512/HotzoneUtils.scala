package cse512

object HotzoneUtils {

  def ST_Contains(queryRectangle: String, pointString: String ): Boolean = {

    val target_point = pointString.split(",")
    val points_rec = queryRectangle.split(",")

    val targetX = target_point(0).toDouble
    val targetY = target_point(1).toDouble

    val x1 = points_rec(0).toDouble
    val y1 = points_rec(1).toDouble
    val x2 = points_rec(2).toDouble
    val y2 = points_rec(3).toDouble

    val min_recX: Double = math.min(x1,x2)
    val max_recX: Double = math.max(x1,x2)
    val min_recY: Double = math.min(y1,y2)
    val max_recY: Double = math.max(y1,y2)

    if ((targetX <= max_recX) && (targetX >= min_recX) && (targetY <= max_recY) && (targetY >= min_recY)){
      return true
    }
    else{
      return false
    }
    // YOU NEED TO CHANGE THIS PART
  }

}
