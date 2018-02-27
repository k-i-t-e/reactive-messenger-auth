package controllers

import javax.inject.{Inject, Singleton}

import dao.UserRepository
import entities.{RestResult, User}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepository: UserRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "userName").write[String] and
        (JsPath \ "password").writeNullable[String]
  )(unlift(User.unapply))

  implicit val userReads: Reads[User]= (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "userName").read[String] and
      (JsPath \ "password").readNullable[String]
  )(User.apply _)

  implicit val resultBooleanWrites = Json.writes[RestResult[Boolean]]
  implicit val resultUserWrites = Json.writes[RestResult[User]]

  def getContactsList(userId: Long) = Action.async {
    _ => {
      userRepository.getUsers().map(u => Ok(Json.toJson(u)))
    }
  }

  def validateUser = parse.json.validate(v => v.validate.asEither.left.map(e => BadRequest(JsError.toJson(e))))

  def register = Action(validateUser).async {
    request => {
      val user = request.body
      userRepository.createUser(user).map(u => Ok(Json.toJson(RestResult[User](u))))
    }
  }

  def login = Action(validateUser).async {
    req => {
      val userRequest = req.body
      userRepository.findUser(userRequest).map {
        case Some(u) => Ok(Json.toJson(u.hidePassword))
        case None => Unauthorized
      }
    }
  }
}

