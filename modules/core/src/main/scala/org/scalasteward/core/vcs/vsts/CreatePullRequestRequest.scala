package org.scalasteward.core.vcs.vsts

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import org.scalasteward.core.git.Branch
import org.scalasteward.core.vcs.data.NewPullRequestData

private[vsts] case class ResourceRef(id: String, url: String)
private[vsts] case class Reviewer(id: String)
private[vsts] case class CreatePullRequestRequest(sourceRefName: Branch,
                                                  targetRefName: Branch,
                                                  title: String,
                                                  description: String,
                                                  reviewers: List[Reviewer],
                                                  workItemRefs: List[ResourceRef]
                                                 )

private[vsts] object CreatePullRequestRequest {
  def apply(data: NewPullRequestData): CreatePullRequestRequest = {
    CreatePullRequestRequest(
      Branch(s"refs/heads/${data.head}"),
      data.base.copy(name = s"refs/heads/${data.base.name}"),
      data.title,
      data.body,
      List(Reviewer("d2d6e9e7-a489-67a9-9969-0bef18f2df99")), //FIXME: Make it a config param
      data.workItems.map(id => ResourceRef(id))
    )
  }
  implicit val createPullRequestEncoder: Encoder[CreatePullRequestRequest] = deriveEncoder
  implicit val reviewerEncoder: Encoder[Reviewer] = deriveEncoder
}

private[vsts] object ResourceRef {
  def apply(id: String): ResourceRef = {
    ResourceRef(
      id,
      s"https://copavsts.visualstudio.com/_apis/wit/workItems/$id"
    )
  }
  implicit val resourceRefEncoder: Encoder[ResourceRef] = deriveEncoder
}
