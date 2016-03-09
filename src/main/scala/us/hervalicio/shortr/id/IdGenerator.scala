package us.hervalicio.shortr.id

import com.twitter.util.Future

/**
  * Generates new unique ids.
  *
  * Created by herval on 3/9/16.
  */
trait IdGenerator {

  def nextId(): Future[Id]

}
