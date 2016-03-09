package us.hervalicio.shortr.storage.memory

import java.util
import java.util.concurrent.ConcurrentHashMap
import scala.collection._
import us.hervalicio.shortr.storage.KeyValueStorage

import collection.JavaConversions._

/**
  * A fake in-memory Redis, to be replaced by the real deal when it's provided
  *
  * Created by herval on 3/9/16.
  */
class InMemoryKeyValueStorage extends KeyValueStorage {

  private val data: concurrent.Map[String, Any] = new ConcurrentHashMap[String, Any]()

  override def get[O](key: String): Option[O] = {
    Thread.sleep(1) // simulating some network latency
    data.get(key).map(a => a.asInstanceOf[O])
  }

  override def put[T](key: String, obj: T) = {
    Thread.sleep(1) // simulating some network latency
    data.put(key, obj.asInstanceOf[Any])
  }

  override def incr(key: String): Long = synchronized {
    Thread.sleep(1) // simulating some network latency
    val value = data.getOrElse(key, 0l).asInstanceOf[Long]+1
    data.put(key, value.asInstanceOf[Long])
    value
  }

  override def append[O](key: String, obj: O): Unit = {
    Thread.sleep(1) // simulating some network latency
    val value = data.getOrElse(key, new util.ArrayList[O]()).asInstanceOf[util.ArrayList[O]]
    value.add(obj)
    data.put(key, value)
  }

  override def list[O](key: String): List[O] = {
    Thread.sleep(1) // simulating some network latency
    val value = data.getOrElse(key, new util.ArrayList[O]()).asInstanceOf[util.ArrayList[O]]
    value.toList
  }
}
