package us.hervalicio.shortr.id.timebased

import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}
import java.util.function.LongUnaryOperator

import com.twitter.util.Future
import us.hervalicio.shortr.id.{Id, IdGenerator}

/**
  * A very simple Id generator that relies on timestamp + a sequential identifier + a machine identifier
  * to allow generating multiple ids on the same second.
  *
  * Created by herval on 3/9/16.
  */
class UUIDGenerator(machineId: MachineIdentifier) extends IdGenerator {

  class TimestampGenerator() {
    val beginningOfTime = 1456321773L // "custom" epoch allows for smaller timestamps being generated from Feb 24 2016 on - will fill up 41 bits by 20 the year 2085

    def next(): Long = {
      System.currentTimeMillis() - beginningOfTime
    }
  }

  class SequenceGenerator(max: Int) {
    val current = new AtomicLong(0)
    var prevTimestamp = System.nanoTime()

    def next(): Long = synchronized {
      while (current.get() == max-1 && prevTimestamp == System.currentTimeMillis()) {
        // generating too fast! Spin wheels a bit.
        Thread.sleep(1)
      }

      prevTimestamp = System.currentTimeMillis()
      current.getAndUpdate(new LongUnaryOperator {
        override def applyAsLong(operand: Long): Long = {

          if (operand == max-1) {
            0
          } else {
            operand + 1
          }
        }
      })
    }
  }

  private val seq = new AtomicInteger()
  private val timestampGenerator = new TimestampGenerator()
  private val sequenceGenerator = new SequenceGenerator(4096)

  override def nextId(): Future[Id] = Future {
    val n = timestampGenerator.next() << 22 | // 41 bits of timestamp in milissecond precision
        machineId.boundedId << 12 | // 10 bit machine id = 1024 possible ids
        sequenceGenerator.next() // 12 bit sequence = 4096 numbers per timestamp
    Id(n)
  }
}