package us.hervalicio.shortr.stats

import com.twitter.util.Future
import us.hervalicio.shortr.id.Id

/**
  * Created by herval on 3/9/16.
  */
trait StatsStorage  {

  def click(url: Id, timestamp: Long): Future[Unit]

  def clicks(url: Id): Future[List[Click]]

}
