package us.hervalicio.shortr.id

import com.twitter.util.Await
import org.scalatest.FunSuite
import us.hervalicio.shortr.storage.memory.InMemoryKeyValueStorage

/**
  * Created by herval on 3/9/16.
  */
class IdGeneratorClientTest extends FunSuite {
  val generator = new IdGeneratorClient(new InMemoryKeyValueStorage())

  test("generates only unique numbers") {
    val all = (1 to 10).map(_ => generator.nextId()).map(f => Await.result(f)).toList
    val uniques = all.toSet
    val q1 = uniques.size
    val q2 = all.size
    assert(q1 == q2)
  }

}
