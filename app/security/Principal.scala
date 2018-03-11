package security

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

case class Principal(loginInfo: LoginInfo,
                     userName: String,
                     userId: Long,
                     password: Option[PasswordInfo]) extends Identity

