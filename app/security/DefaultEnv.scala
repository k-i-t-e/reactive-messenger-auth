package security

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import entities.User

trait DefaultEnv extends Env {
  override type I = User
  override type A = JWTAuthenticator
}
