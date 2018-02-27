package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dao.UserRepository
import entities.{RestResult, User}
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import security.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               userRepository: UserRepository,
                               silhouette: Silhouette[DefaultEnv],
                               credentialsProvider: CredentialsProvider)
                              (implicit ec: ExecutionContext) extends AbstractAuthController(cc) {
  def login = Action(validateUser).async {
    req => {
      val userRequest = req.body
      userRequest.password match {
        case Some(password) => {
          for {
            auth <- credentialsProvider.authenticate(Credentials(userRequest.userName, password))
            user <- userRepository.findUser(userRequest)
          } yield {
            System.out.println(auth.providerID)
            user match {
              case Some(u) => Ok(Json.toJson(RestResult[User](u.hidePassword)))
              case None => Unauthorized
            }
          }
          /*credentialsProvider.authenticate(Credentials(userRequest.userName, password)).map(info => {
            System.out.println(info.providerID)
          })
          userRepository.findUser(userRequest).map {
           case Some(u) => Ok(Json.toJson(RestResult[User](u.hidePassword)))
           case None => Unauthorized
         }*/
        }
        case None => Future.successful(Unauthorized)
      }
    }
  }
}
