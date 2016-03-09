package us.hervalicio.shortr.storage.memory

/**
  * A fake in-memory Redis, to be replaced by the real deal when it's provided
  *
  * Created by herval on 3/9/16.
  */
class InMemoryKeyValueStorage {

  private val data: scala.collection.mutable.Map[String, AnyVal] = new scala.collection.mutable.HashMap()

  def get[O](key: String): Option[O] = {
    Thread.sleep(5) // simulating some network latency
    data.get(key).map(a => a.asInstanceOf[O])
  }

  def put[T](key: String, obj: T) = {
    Thread.sleep(5) // simulating some network latency
    data.put(key, obj.asInstanceOf[AnyVal])
  }

  def incr(key: String): Long = synchronized {
    Thread.sleep(5) // simulating some network latency
    val value = data.getOrElse(key, 0l).asInstanceOf[Long]+1
    data.put(key, value)
    value
  }

}
