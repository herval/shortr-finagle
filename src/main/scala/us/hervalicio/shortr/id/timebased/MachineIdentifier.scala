package us.hervalicio.shortr.id.timebased

/**
  * Created by herval on 3/9/16.
  */
case class MachineIdentifier(fullId: Int) {
  val boundedId: Int = fullId & 0x03FF // only 10 bits -> restrict to 1024 possible ids
}
