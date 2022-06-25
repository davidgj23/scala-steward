package org.scalasteward.core.vcs.azure

import io.circe.Decoder
import org.http4s.Uri
import org.scalasteward.core.git.Branch
import org.scalasteward.core.util.uri._

final private[azure] case class RepositoryResponse(
                                                        id: String,
                                                        name: String,
                                                        url: Uri,
                                                        remoteUrl: Uri,
                                                        defaultBranch: Branch
                                                      )

private[azure] object RepositoryResponse {
  implicit val decoder: Decoder[RepositoryResponse] = Decoder.instance { c =>
    for {
      id <- c.downField("id").as[String]
      name <- c.downField("name").as[String]
      url <- c.downField("url").as[Uri]
      remoteUrl <- c.downField("remoteUrl").as[Uri]
      defaultBranch <- c.downField("defaultBranch").as[Branch]
    } yield RepositoryResponse(id, name, url, remoteUrl, defaultBranch.copy(name = defaultBranch.name))
  }
}
