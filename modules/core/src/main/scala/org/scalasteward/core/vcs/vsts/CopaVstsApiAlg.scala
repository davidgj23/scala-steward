/*
 * Copyright 2018-2022 Scala Steward contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scalasteward.core.vcs.vsts

import cats.MonadThrow
import cats.syntax.all._
import org.http4s.Request
import org.scalasteward.core.application.Config.VCSCfg
import org.scalasteward.core.git.Branch
import org.scalasteward.core.util.HttpJsonClient
import org.scalasteward.core.vcs.VCSApiAlg
import org.scalasteward.core.vcs.data._

/** https://developer.atlassian.com/bitbucket/api/2/reference/ */
class CopaVstsApiAlg[F[_]](config: VCSCfg, modify: Repo => Request[F] => F[Request[F]])(implicit
                                                                                        client: HttpJsonClient[F],
                                                                                        F: MonadThrow[F]
) extends VCSApiAlg[F] {
  private val url = new Url(config.apiHost)

  override def createFork(repo: Repo): F[RepoOut] =
    ???

  override def createPullRequest(repo: Repo, data: NewPullRequestData): F[PullRequestOut] = {
    val payload = CreatePullRequestRequest(
      Branch(data.head),
      data.base,
      data.title,
      data.body,
      List(Reviewer("d2d6e9e7-a489-67a9-9969-0bef18f2df99"))
    )
    client.postWithBody(url.pullRequests(repo), payload, modify(repo))
  }

  override def getBranch(repo: Repo, branch: Branch): F[BranchOut] =
    client.get(url.branch(repo, branch), modify(repo))

  override def getRepo(repo: Repo): F[RepoOut] =
    for {
      repo <- client.get[RepositoryResponse](url.repo(repo), modify(repo))
    } yield mapToRepoOut(repo)

  private def mapToRepoOut(repo: RepositoryResponse): RepoOut =
    RepoOut(
      repo.name,
      UserOut("dgiraldo"),
      None,
      repo.remoteUrl,
      repo.defaultBranch
    )

  override def listPullRequests(repo: Repo, head: String, base: Branch): F[List[PullRequestOut]] =
    client.get(url.listPullRequests(repo), modify(repo))

  override def closePullRequest(repo: Repo, number: PullRequestNumber): F[PullRequestOut] =
    client.post[PullRequestOut](
      url.decline(repo, number),
      modify(repo)
    )

  override def commentPullRequest(
                                   repo: Repo,
                                   number: PullRequestNumber,
                                   comment: String
                                 ): F[Comment] =
    ???
}
