package us.hervalicio.shortr.id

import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

import com.twitter.util.Future

/**
  * A very simple Id generator that relies on timestamp + a sequential identifier + a machine identifier
  * to allow generating multiple ids on the same second.
  *
  * Created by herval on 3/9/16.
  */
class TimestampBasedGenerator(machineId: Int) extends IdGenerator {

  private val seq = new AtomicInteger()

  override def next(): Future[Id] = Future {
    Id(new Date().getTime) // TODO
  }
}