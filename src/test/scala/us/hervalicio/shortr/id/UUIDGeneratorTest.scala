package us.hervalicio.shortr.id

import com.twitter.util.Await
import org.scalatest.FunSuite

/**
  * Created by herval on 3/9/16.
  */
class UUIDGeneratorTest extends FunSuite {
  val generator = new UUIDGenerator(MachineIdentifier(1))

  test("generates only unique numbers") {
    val all = (1 to 100000).map(_ => generator.next()).map(f => Await.result(f)).toList
    val uniques = all.toSet
    val q1 = uniques.size
    val q2 = all.size
    assert(q1 == q2)
  }

}
