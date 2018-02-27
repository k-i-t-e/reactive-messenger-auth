package security

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

case class Principal(loginInfo: LoginInfo, userName: String, password: Option[PasswordInfo]) extends Identity

