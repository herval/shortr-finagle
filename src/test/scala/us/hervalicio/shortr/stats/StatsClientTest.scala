package us.hervalicio.shortr.stats

import com.twitter.util.Await
import org.scalatest.FunSuite
import us.hervalicio.shortr.id.Id
import us.hervalicio.shortr.storage.memory.InMemoryKeyValueStorage

/**
  * Created by herval on 3/9/16.
  */
class StatsClientTest extends FunSuite {

  val storage = new InMemoryKeyValueStorage()
  val client = new StatsClient(storage)

  test("save & retrieve clicks") {
    val id = Id(123)
    val time = System.currentTimeMillis()
    Await.result(client.click(id, time))

    val stored = Await.result(client.clicks(id))
    assert(stored == List(Click(time)))
  }
}
