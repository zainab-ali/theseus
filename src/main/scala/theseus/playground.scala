package playground

case class Fix[F[_]](unfix: F[Fix[F]])

//provided that the input events are the same
case class Route[R](s: String => R)

case class InfoA(info: String)
case class InfoB(info: String)

object example {

  case class Foo[A](f: String => A)

  def recurseApple: Fix[Foo] = Fix(Foo({
    case "banana" => recurseBanana
    case _ => recurseApple
  }))

  def recurseBanana: Fix[Foo] = Fix(Foo({
    case "apple" => recurseApple
    case _ => recurseBanana
  }))

  def apple: String => Option[InfoA] = ???
  def banana: String => Option[InfoB] = ???

  def state1: Fix[Route] = Fix(Route({
    case s if(apple(s).nonEmpty) => state2
    case s if(banana(s).nonEmpty) => state3
  }))

  def state2: Fix[Route] = Fix(Route({
    case s => state1
  }))

  def state3: Fix[Route] = Fix(Route({
    case s => state1
  }))

}

/**
  * write functions from E => Info
  * combine the functions from E => Info ... refer to other functions from E => other and fix
  * 
  * */
