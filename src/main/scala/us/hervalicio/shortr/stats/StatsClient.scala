package us.hervalicio.shortr.stats

import com.twitter.util.Future
import us.hervalicio.shortr.id.Id
import us.hervalicio.shortr.storage.KeyValueStorage

/**
  * Created by herval on 3/9/16.
  */
class StatsClient(storage: KeyValueStorage) extends StatsStorage {

  override def click(url: Id, timestamp: Long): Future[Unit] = Future {
    storage.append(key(url), Click(timestamp))
  }

  override def clicks(url: Id): Future[List[Click]] = Future {
    storage.list[Click](key(url))
  }

  private def key(url: Id) = "clicks_" + url.number.toString
}
