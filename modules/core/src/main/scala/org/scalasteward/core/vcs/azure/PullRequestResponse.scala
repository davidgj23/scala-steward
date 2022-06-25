package org.scalasteward.core.vcs.azure

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.Uri
import org.scalasteward.core.util.uri._

final private[azure] case class PullRequestResponse(value: List[PullRequestValue])

final private[azure] case class PullRequestValue(pullRequestId: Int, status: String, url: Uri, title: String)

private[azure] object PullRequestResponse{
  implicit val responseDecoder: Decoder[PullRequestResponse] = deriveDecoder
}

private[azure] object PullRequestValue{
  implicit val valueDecoder: Decoder[PullRequestValue] = Decoder.instance { c =>
    for {
      id <- c.downField("pullRequestId").as[Int]
      status <- c.downField("status").as[String]
      url <- c.downField("url").as[Uri]
      title <- c.downField("title").as[String]
    } yield PullRequestValue(id, status, url, title)
  }
}
