package security

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.UserRepository
import entities.User
import play.api.mvc.Request

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PrincipalIdentityService @Inject() (userRepository: UserRepository,
                                          passwordHasher: PasswordHasher)(implicit ec: ExecutionContext) extends UserDetailsService {
  override def retrieve(loginInfo: LoginInfo): Future[Option[Principal]] = {
    userRepository.findUser(User(None, loginInfo.providerID, Option(loginInfo.providerKey)))
      .map(opt => opt.flatMap(u => Option(Principal(loginInfo, u.userName, None))))
  }

  /**
    * Creates a user
    * @param user
    * @param request
    * @return
    */
  def create(user: User)(implicit request: Request[_]): Future[User] = {
    val loginInfo = LoginInfo(CredentialsProvider.ID, user.userName)
    val passwordInfo = passwordHasher.hash(user.password.get)

    for {
      newUser <- userRepository.createUser(user)
      newPasswordInfo <- userRepository.save(loginInfo, passwordInfo)
      //authenticator <- silhouette.env.authenticatorService.create(loginInfo)
      //token <- silhouette.env.authenticatorService.init(authenticator)
    } yield {
      newUser
    }
  }
}
