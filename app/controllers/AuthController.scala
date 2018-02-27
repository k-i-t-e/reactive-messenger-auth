package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.{LoginEvent, Silhouette}
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.UserRepository
import entities.{RestResult, User, UserAndToken}
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import security.{DefaultEnv, Principal}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               userRepository: UserRepository,
                               silhouette: Silhouette[DefaultEnv],
                               credentialsProvider: CredentialsProvider)
                              (implicit ec: ExecutionContext) extends AbstractAuthController(cc) {

  def login = Action(validateUser).async {
    implicit req => {
      val userRequest = req.body
      userRequest.password match {
        case Some(password) => {
          credentialsProvider.authenticate(Credentials(userRequest.userName, password)).flatMap(auth =>
            silhouette.env.authenticatorService.create(auth).flatMap(authenticator =>
              silhouette.env.authenticatorService.init(authenticator).map(token => {
                silhouette.env.eventBus.publish(LoginEvent(Principal(auth, userRequest.userName, None), req))
                Ok(Json.toJson(RestResult[UserAndToken](UserAndToken(User(None, auth.providerKey, None), token))))
              })
            )
          ).recover({ case _ => Unauthorized })
        }
        case None => Future.successful(Unauthorized)
      }
    }
  }

  def check = silhouette.SecuredAction.async {
    implicit request => {
      Future.successful(Ok(Json.toJson(request.identity.userName)))
    }
  }
}
