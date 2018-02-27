package security

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import play.api.mvc.Request

import scala.concurrent.Future

object Roles {
  sealed abstract class Role(val name: String)
  case object RoleAdmin extends Role("ADMIN")
  case object RoleUser extends Role("USER")
}

case class Authenticated() extends Authorization[Principal, DefaultEnv#A] {
  override def isAuthorized[B](identity: Principal, authenticator: JWTAuthenticator)(implicit request: Request[B]): Future[Boolean] = {
    Future.successful(true)
  }
}