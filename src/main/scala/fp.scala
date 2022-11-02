package demo


// Scala supports first-class and higher-order functions as known
// from ISL in Info 1.
object demoHigherOrderFunctions {

  // higher-order functions
  def compose(f: Int => Int, g: Int => Int)(n: Int): Int = g(f(n))

  // first-class functions
  compose(x => x + 1, y => y * 2)(42) // 86

  // **single argument** sections can also be invoked with braces!
  compose(x => x + 1, y => y * 2) {
    42
  }

  // this is particularly useful for functions
  def runWithSomeValues(fun: Int => Int) = fun(1) + fun(2)

  runWithSomeValues { n =>
    println(s"I have been called with ${ n }")
    n * 2
  }
}

// By default many collections in Scala are *immutable*.
object demoImmutable {

  // Lists
  // -----
  val l = List(1, 2, 3)
  def sum(l: List[Int]): Int = l match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }

  sum(l)

  // important library functions
  l.map { x => x > 2 } // List(false, false, true)
  l.filter { x => x > 2 } // List(2)
  l.collect { case x if x > 2 => x * 2 } // List(6)

  l.map { x => List(x) } // List(List(1), List(2), List(3))
  l.map { x => List(x) }.flatten // List(1, 2, 3)
  l.flatMap { x => List(x) } // List(1, 2, 3)

  for (element <- l) {
    println(element)
  }
  // ===
  l.foreach { element => println(element) }

  for (element <- l)
    yield element > 2
  // ===
  l.map { x => x > 2 }


  // Sets
  // ----
  // there is many more collections, like maps and sets...
  val mySet = Set(0, 0, 1, 2, 3).intersect(Set(1, 3, 4)) // Set(1, 3)


  // Option
  // ------
  val maybeFirst = l.find { x => x > 2 }
  maybeFirst match {
    case Some(value) => println(s"Value found ${ value }")
    case None => println("No value found")
  }

  def saveDiv(n: Int, m: Int): Option[Int] =
    if (m == 0) None
    else Some(n / m)
}

object demoRecords {

  // Case classes / structs
  //
  // In BSL, this would be
  //   (define-struct person (name favoriteFood))
  case class Person(name: String, favoriteFood: String)

  // here p.name is like (person-name p) in BSL
  val p = Person("Jonathan", "Burgers")
  println(s"Hello ${p.name}. I see you like ${p.favoriteFood}")

  // also like in Racket, we can use pattern matching to extract information
  // from records / structs / case-classes.
  p match {
    case Person(name, food) => println("Hello" + name)
  }
}


// In Info 1 you have learnt about sumtypes (also sometimes called "union types").
// For example:
//
// A traffic light is one of:
// - red
// - yellow
// - green

// In Scala, we have multiple ways of modeling this.
// 1) As an enumeration (e.g., enum ... { case A; case B })
// 2) By using type union (e.g., A | B)
// 3) By using subtyping.
object demoEnumeration {

  // 1) Enumeration
  enum TrafficLight {
    case Red
    case Yellow
    case Green
  }

  def switchTrafficLight(t: TrafficLight): TrafficLight = t match {
    case TrafficLight.Red => TrafficLight.Green
    case TrafficLight.Yellow => TrafficLight.Red
    case TrafficLight.Green => TrafficLight.Yellow
  }

  println(switchTrafficLight(TrafficLight.Green)) // Yellow
}

object demoUnionTypes {

  // Let us assume:
  case class Red()
  case class Yellow()
  case class Green()

  // If we already have some types defined, we translate the BSL comment into a
  // Scala type declaration:
  type TrafficLight = Red | Yellow | Green

  def switchTrafficLight(t: TrafficLight): TrafficLight = t match {
    case Red() => Green()
    case Yellow() => Red()
    case Green() => Yellow()
  }

  println(switchTrafficLight(Green())) // Yellow()
}

object demoSubtypes {

  // Finally, we can use Scala's subtyping feature to express that "Red is a TrafficLight".
  sealed trait TrafficLight
  case class Red() extends TrafficLight
  case class Yellow() extends TrafficLight
  case class Green() extends TrafficLight

  def switchTrafficLight(t: TrafficLight): TrafficLight = t match {
    case Red() => Green()
    case Yellow() => Red()
    case Green() => Yellow()
  }

  println(switchTrafficLight(Green()))
}


// More on Pattern Matching
// ------------------------
// Pattern matching in Scala is a very important feature.
// It replaces instanceof checks from other languages like Java.

object demoPatternmatching {

  case class Person(name: String, favoriteFood: String)

  sealed trait TrafficLight
  case class Red() extends TrafficLight
  case class Yellow() extends TrafficLight
  case class Green() extends TrafficLight

  case class Crossing(horizontal: TrafficLight, vertical: TrafficLight)

  def isAllowedConfig(c: Crossing): Boolean = c match {
    case Crossing(Green(), Green()) => false
    case Crossing(Yellow(), Green()) => false
    case Crossing(Green(), Yellow()) => false

    // this is called
    case _ => true
  }

  println(isAllowedConfig(Crossing(Yellow(), Green()))) // false
  println(isAllowedConfig(Crossing(Yellow(), Red()))) // true

  // we can also pattern match on something, but also remember the original value:
  def isAllowedConfig2(c: Crossing): Boolean = c match {
    case Crossing(c1 @ Green(), c2 @ Green()) => println(s"Is allowed: ${c1} and ${c2}"); false
    case Crossing(Yellow(), Green()) => false
    case Crossing(Green(), Yellow()) => false

    // this is called
    case _ => true
  }
}
