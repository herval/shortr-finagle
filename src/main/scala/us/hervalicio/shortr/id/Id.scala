package us.hervalicio.shortr.id

/**
  * Created by herval on 3/9/16.
  */
case class Id(number: Long) {
  def readableString: String = BigInt(number).toString(36)
}

object Id {
  def fromReadableString(id: String) = Id(BigInt(id, 36).toLong)
}
