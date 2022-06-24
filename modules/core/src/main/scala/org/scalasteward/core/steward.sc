import cats.Semigroup
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.Uri

uri"https://copavsts.visualstudio.com/copa-ebusiness-solutions-src/_apis/git/repositories/booking/refs?Ffilter=heads%252F/refs%2Fheads%2Fdev"


import cats.kernel.instances.string._
val res: Option[String] = Semigroup[String].combineAllOption(List("One ", "Two ", "Three"))
