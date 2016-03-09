package us.hervalicio.shortr.storage

/**
  * Created by herval on 3/9/16.
  */
trait KeyValueStorage {

  def get[O](key: String): Option[O]

  def put[T](key: String, obj: T)

  def incr(key: String): Long

}
