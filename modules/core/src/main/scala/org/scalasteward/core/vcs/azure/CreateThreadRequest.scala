package org.scalasteward.core.vcs.azure

import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}

import java.time.{ZoneId, ZonedDateTime}
import java.util.UUID

private[azure] case class Property[A](`type`: String, value: A)
private[azure] case class Properties(supportsMarkDown: Property[Int], uniqueId: Property[String])
private[azure] case class Comment(commentType: Int, content: String)
private[azure] case class CreateThreadRequest(id: Int = -1,
                                              comments: List[Comment],
                                              lastUpdatedDate: String,
                                              publishedDate: String,
                                              status: Int = 1,
                                              properties: Properties,
                                                 )

private[azure] object CreateThreadRequest {
  def apply(commentContent: String): CreateThreadRequest = {
    val updateDate = ZonedDateTime.now(ZoneId.of("GMT")).toString.replace("[GMT]","")
    val supportsMarkDown = Property("System.Int32", 1)
    val uniqueId = Property("System.String", UUID.randomUUID().toString)
    val properties = Properties(supportsMarkDown, uniqueId)
    val comments = List(Comment(1, commentContent))
    CreateThreadRequest(
      comments = comments,
      lastUpdatedDate = updateDate,
      publishedDate = updateDate,
      properties = properties,
    )
  }
  implicit val propertyIntEncoder: Encoder[Property[Int]] = deriveEncoder
  implicit val propertyStringEncoder: Encoder[Property[String]] = deriveEncoder
  implicit val propertiesEncoder: Encoder[Properties] = Encoder.instance { d =>
    Json.obj(
      ("Microsoft.TeamFoundation.Discussion.SupportsMarkdown", d.supportsMarkDown.asJson),
      ("Microsoft.TeamFoundation.Discussion.UniqueID", d.uniqueId.asJson)
    )
  }
  implicit val commentEncoder: Encoder[Comment] = deriveEncoder
  implicit val createThreadEncoder: Encoder[CreateThreadRequest] = deriveEncoder
}
