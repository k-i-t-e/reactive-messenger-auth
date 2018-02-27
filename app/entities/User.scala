package entities

import com.mohiva.play.silhouette.api.Identity

case class User(id: Option[Long], userName: String, password: Option[String] = None) extends Identity {
  def hidePassword: User = User(id, userName)
}


