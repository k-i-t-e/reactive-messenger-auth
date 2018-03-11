package controllers.vo

import entities.User

case class UserAndToken(user: User, token: String)
