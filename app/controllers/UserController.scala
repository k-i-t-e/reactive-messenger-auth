package controllers

import com.mohiva.play.silhouette.api.Silhouette
import controllers.vo.RestResult
import entities.User
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import security.{DefaultEnv, PrincipalIdentityService}
import services.UserService

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               userService: UserService,
                               silhouette: Silhouette[DefaultEnv])(implicit ec: ExecutionContext)
  extends AbstractAuthController(cc) {
  def getContactsList = silhouette.SecuredAction.async {
    implicit request => userService.getContactList().map(list => Ok(Json.toJson(RestResult[Seq[User]](list))))
  }

  def addContact(contactId: Long) = silhouette.SecuredAction.async {
    implicit request => userService.addContact(contactId).map(_ => Ok(Json.toJson(RestResult(true))))
  }

  def deleteContact(contactId: Long) = silhouette.SecuredAction.async {
    implicit request => userService.deleteContact(contactId).map(_ => Ok(Json.toJson(RestResult(true))))
  }

  def searchContacts(name: String) = silhouette.SecuredAction.async {
    request => userService.findUsers(name).map(users => Ok(Json.toJson(RestResult(users))))
  }
}

