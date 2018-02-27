package controllers

import javax.inject.{Inject, Singleton}

import dao.UserRepository
import entities.{RestResult, User}
import play.api.libs.json._
import play.api.mvc._
import security.PrincipalIdentityService

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               userRepository: UserRepository,
                               principalIdentityService: PrincipalIdentityService)(implicit ec: ExecutionContext)
  extends AbstractAuthController(cc) {
  def getContactsList(userId: Long) = Action.async {
    _ => {
      userRepository.getUsers().map(u => Ok(Json.toJson(RestResult[Seq[User]](u))))
    }
  }

  def register = Action(validateUser).async {
    implicit request => {
      val user = request.body
      principalIdentityService.create(user).map(u => Ok(Json.toJson(RestResult[User](u))))
    }
  }
}

