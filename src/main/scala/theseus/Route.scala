// package theseus

// import scala.reflect.ClassTag
// import monocle._
// import monocle.macros._


// // final class Route[E, S, O](f: (E, S) => Option[(O, S, Route[E, S, O])]) {

// //   def contramap[E1](g: E1 => E): Route[E1, S, O] =
// //     new Route((e1, s) => f(g(e1), s).map { case (o, s, r) => (o, s, r.contramap(g)) })

// //   def pcontramapOpt[E1](g: E1 => Option[E]): Route[E1, S, O] =
// //     new Route((e1, s) => g(e1).flatMap(e => f(e, s)).map { case (o, s, r) => (o, s, r.pcontramapOpt(g)) } )

// //   def pcontramap[E1](g: PartialFunction[E1, E]): Route[E1, S, O] =
// //      pcontramapOpt(g.lift)

// //   def map[O1](g: O => O1): Route[E, S, O1] =
// //     new Route((e, s) => f(e, s).map {
// //       case (o, s, r) => (g(o), s, r.map(g))
// //     })

// //   // def asPull[F[_]](h: Handle[F, E], s: S): Pull[F, O, Nothing] =
// //   //   h.await1 {
// //   //     case (e, h) => f(e, s) match {
// //   //       case Some((o, s, r)) => Pull.output(o) >> r.asPull(h, s)
// //   //       case None => asPull(h, s)
// //   //     }
// //   //   }
// // }

// // object Route {

// //   def apply[E, S, O](f: PartialFunction[(E, S), (O, S, Route[E, S, O])]): Route[E, S, O] =
// //     new Route((e, s) => f.lift((e, s)))


// // }

// final class Path[E, IA, SA, IB, SB, O](val apply: (E, IA) => Option[(IB, O)]) {

//   def contramap[E1](f: E1 => E): Path[E1, IA, SA, IB, SB, O] = new Path((e1, ia) => apply(f(e1), ia))

//   def pcontramapOpt[E1](f: E1 => Option[E]): Path[E1, IA, SA, IB, SB, O] = new Path((e1, ia) => f(e1).flatMap(e => apply(e, ia)))

//   def pcontramap[E1](f: PartialFunction[E1, E]): Path[E1, IA, SA, IB, SB, O] = pcontramapOpt(f.lift)
//   //def combine[E1, IB1, SB1](p: Path[E1, IA, SA, IB1, SB1, O]): Path[E Either E1, IA, SA, IB Either IB1, SB Either SB1] = ???
// }

// object Path {

//   def apply[E, IA, SA, IB, SB, O](f: PartialFunction[(E, IA), (IB, O)]): Path[E, IA, SA, IB, SB, O] =
//     new Path((e, ia) => f.lift((e, ia)))

// }

// trait SingleDecision[IA, E, O] {
//   type Out = SB
//   type Info = I

//   def apply(e: E, ia: IA): Option[(I, O)]
// }

// final class Decision[IA, SA, C <: Coproduct](c: C)

// object test {

//   trait Default
//   trait Command

//   sealed trait State
//   case class World(people: List[Citizen], shrubs: List[Shrub], trees: List[Tree]) extends State
//   case class CommandingCitizen(target: Citizen, world: World) extends State

//   case class Citizen(name: String)
//   case class Shrub(name: String)
//   case class Tree(name: String)

//   sealed trait Event
//   case class CommandCitizen(citizen: Citizen) extends Event
//   case class PickShrub(shrub: Shrub) extends Event
//   case class ChopTree(tree: Tree) extends Event

//   sealed trait InputEvent
//   case class Key(key: String) extends InputEvent
//   case class Click(x: Int, y: Int) extends InputEvent

//   def commandCitizen: Path[CommandCitizen, World, Default, CommandingCitizen, Command, String] = Path {
//     case (c, w) => (CommandingCitizen(c.citizen, w), "commanding citizen")
//   }

//   commandCitizen.pcontramap[InputEvent] {
//     case Key("C") => CommandCitizen(Citizen("Bob"))
//   }

//   def pickShrub: Path[PickShrub, CommandingCitizen, Command, World, Default, String] = Path {
//     case (e, s) => (s.world, "picking shrub")
//   }

//   def chopTree: Path[ChopTree, CommandingCitizen, Command, World, Default, String] = Path {
//     case (e, s) => (s.world, "chopping tree")
//   }

//   // def idle: Route[Event, State, String] = Route {
//   //   case (c: CommandCitizen, w: World) =>
//   //     ("commanding citizen", CommandingCitizen(c.citizen, w), command)
//   // }
//   // //problem: have to define state transitions as a giant group

//   // idle.pcontramap[InputEvent] {
//   //   case Key("c") => CommandCitizen(Citizen("Bob"))
//   //   case Click(1, 2) => PickShrub(Shrub("garlic"))
//   //   case Click(2, 3) => ChopTree(Tree("apple"))
//   // }

//   // def command: Route[Event, State, String] = Route {
//   //   case (PickShrub(shrub), s: CommandingCitizen) =>
//   //     ("picking shrub", s.world, idle)
//   //   case (ChopTree(tree), s: CommandingCitizen ) =>
//   //     ("chopping tree", s.world, idle)
//   // }
// }
