package org.scalasteward.core.vcs.azure

trait PropertyMagnet {
  type Value
  def apply(): Property[Value]
}

object PropertyMagnet {
  def createProperty(magnet: PropertyMagnet): Property[magnet.Value] = magnet()

  implicit def intValueProperty(`type`: String, value: Int) = new PropertyMagnet {
    override type Value = Int

    override def apply(): Property[Value] = Property(`type`, value)
  }
  implicit def stringValueProperty(`type`: String, value: String) = new PropertyMagnet {
    override type Value = String

    override def apply(): Property[Value] = Property(`type`, value)
  }
}
