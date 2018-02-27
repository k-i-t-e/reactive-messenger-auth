package security

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.UserRepository
import entities.User

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PrincipalIdentityService @Inject() (userRepository: UserRepository)(implicit ec: ExecutionContext) extends UserDetailsService {
  override def retrieve(loginInfo: LoginInfo): Future[Option[Principal]] = {
    userRepository.findUser(User(None, loginInfo.providerID, Option(loginInfo.providerKey)))
      .map(opt => opt.flatMap(u => Option(Principal(loginInfo, u.userName, None))))
  }

  def create(user: User): Future[User] = {
    val loginInfo = LoginInfo(CredentialsProvider.ID, user.userName)
    userRepository.createUser(user)
  }
}
