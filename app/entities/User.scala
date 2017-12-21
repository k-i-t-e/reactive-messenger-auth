package entities

case class User(id: Option[Long], userName: String, password: Option[String] = None)


