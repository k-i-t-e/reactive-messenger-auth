package exceptions

case class MessengerException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
  def this(message: String) {
    this(message, null)
  }
}
