package org.scalasteward.core.vcs.vsts

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.Uri
import org.scalasteward.core.util.uri._

final private[vsts] case class PullRequestResponse(value: List[PullRequestValue])

final private[vsts] case class PullRequestValue(pullRequestId: Int, status: String, url: Uri, title: String)

private[vsts] object PullRequestResponse{
  implicit val responseDecoder: Decoder[PullRequestResponse] = deriveDecoder
}

private[vsts] object PullRequestValue{
  implicit val valueDecoder: Decoder[PullRequestValue] = Decoder.instance { c =>
    for {
      id <- c.downField("pullRequestId").as[Int]
      status <- c.downField("status").as[String]
      url <- c.downField("url").as[Uri]
      title <- c.downField("title").as[String]
    } yield PullRequestValue(id, status, url, title)
  }
}
