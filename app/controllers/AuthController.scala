package controllers

import javax.inject.{Inject, Singleton}

import dao.UserRepository
import entities.{RestResult, User}
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

@Singleton
class AuthController @Inject()(cc: ControllerComponents, userRepository: UserRepository)(implicit ec: ExecutionContext) extends AbstractAuthController(cc) {
  def login = Action(validateUser).async {
    req => {
      val userRequest = req.body
      userRepository.findUser(userRequest).map {
        case Some(u) => Ok(Json.toJson(RestResult[User](u.hidePassword)))
        case None => Unauthorized
      }
    }
  }
}
