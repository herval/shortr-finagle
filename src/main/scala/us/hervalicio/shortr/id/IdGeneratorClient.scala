package us.hervalicio.shortr.id

import com.twitter.util.Future
import us.hervalicio.shortr.storage.KeyValueStorage

/**
  * Created by herval on 3/9/16.
  */
class IdGeneratorClient(storage: KeyValueStorage) extends IdGenerator {

  override def nextId(): Future[Id] = Future {
    Id(storage.incr("sequential_id"))
  }

}
