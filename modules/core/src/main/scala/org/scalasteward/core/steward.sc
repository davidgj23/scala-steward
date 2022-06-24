import cats.Semigroup
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.Uri
import cats.Eq
import cats.syntax.all._
import cats.syntax._
import org.http4s.syntax.literals._
import org.scalasteward.core.util.unexpectedString
import org.scalasteward.core.vcs.VCSType._

uri"https://copavsts.visualstudio.com/copa-ebusiness-solutions-src/_apis/git/repositories/booking/refs?Ffilter=heads%252F/refs%2Fheads%2Fdev"


import cats.kernel.instances.string._
val res: Option[String] = Semigroup[String].combineAllOption(List("One ", "Two ", "Three"))

val publicWebHost: Some[String] = Some("visualstudio.com")
val host = "https://copavsts.visualstudio.com/copa-ebusiness-solutions-src/_apis/git"
publicWebHost.contains_(host)