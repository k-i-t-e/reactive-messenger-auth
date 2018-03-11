package entities

case class User(id: Option[Long],
                userName: String,
                password: Option[String] = None) {
  def hidePassword: User = User(id, userName)
}


