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
import org.scalasteward.core.git.{Branch, Sha1}
import org.scalasteward.core.util.HttpJsonClient
import org.scalasteward.core.vcs.VCSApiAlg
import org.scalasteward.core.vcs.data._
//import org.typelevel.log4cats.Logger

/** https://developer.atlassian.com/bitbucket/api/2/reference/ */
class CopaVstsApiAlg[F[_]](config: VCSCfg, modify: Repo => Request[F] => F[Request[F]])(implicit
                                                                                        client: HttpJsonClient[F],
                                                                                        //logger: Logger[F],
                                                                                        F: MonadThrow[F]
) extends VCSApiAlg[F] {
  private val url = new Url(config.apiHost)

  override def createFork(repo: Repo): F[RepoOut] =
    ???

  override def createPullRequest(repo: Repo, data: NewPullRequestData): F[PullRequestOut] = {
    val payload = CreatePullRequestRequest(data)
    for{
      response <- client.postWithBody[PullRequestValue, CreatePullRequestRequest](url.pullRequests(repo), payload, modify(repo))
    } yield mapToPullRequestOut(response)
  }

  private def mapToBranchOut(branch: BranchResponse): F[BranchOut] = {
    for{
      value <- F.fromOption(branch.value.headOption, new Exception("No list to map out"))
      commit <- F.fromEither(Sha1.from(value.objectId))
    } yield BranchOut(Branch(value.name), CommitOut(commit))
  }

  override def getBranch(repo: Repo, branch: Branch): F[BranchOut] =
    for{
      branchResponse <- client.get[BranchResponse](url.branch(repo, branch), modify(repo))
      branchOut <- mapToBranchOut(branchResponse)
    } yield branchOut

  override def getRepo(repo: Repo): F[RepoOut] =
    for {
      repo <- client.get[RepositoryResponse](url.repo(repo), modify(repo))
    } yield mapToRepoOut(repo)

  private def mapToRepoOut(repo: RepositoryResponse): RepoOut =
    RepoOut(
      repo.name,
      UserOut("dgiraldo"), //FIXME: make it a param
      None,
      repo.remoteUrl,
      repo.defaultBranch
    )

  override def listPullRequests(repo: Repo, head: String, base: Branch): F[List[PullRequestOut]] = {
    for{
      pullRequestResponse <- client.get[PullRequestResponse](url.listPullRequests(repo, head), modify(repo))
    } yield pullRequestResponse.value.map(mapToPullRequestOut)
  }

  private def mapToPullRequestOut(pullRequest: PullRequestValue): PullRequestOut = {
    val status: PullRequestState = if(pullRequest.status == "active") PullRequestState.Open else PullRequestState.Closed
    PullRequestOut(pullRequest.url, status, PullRequestNumber(pullRequest.pullRequestId), pullRequest.title)
  }

  override def closePullRequest(repo: Repo, number: PullRequestNumber): F[PullRequestOut] =
    for {
      response <- client.patchWithBody[PullRequestValue, UpdatePullRequestRequest](url.decline(repo, number), UpdatePullRequestRequest.closePullRequestRequest, modify(repo))
    } yield mapToPullRequestOut(response)

  override def commentPullRequest(
                                   repo: Repo,
                                   number: PullRequestNumber,
                                   comment: String
                                 ): F[Comment] = {
    val payload = CreateThreadRequest(comment)
    for {
      _ <- client.postWithBody[CreateThreadResponse, CreateThreadRequest](url.threads(repo, number), payload, modify(repo))
    } yield Comment(comment)
  }
}
