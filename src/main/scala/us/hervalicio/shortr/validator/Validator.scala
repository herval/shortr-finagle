package us.hervalicio.shortr.validator

import com.twitter.util.Future

/**
  * Created by herval on 3/9/16.
  */
trait Validator {

  def normalize(url: String): Future[NormalizedURL]

}
