package demo

object demoSubtyping {

  import demoInterfaces.*

  // Subtyping
  // =========

  // This is to show that we can pass an object of type TaxAccount if
  // an Account is expected.
  val savings = new SavingsAccount(10)
    val tax = new TaxAccount(20)
    transfer(100, savings, tax)

  // Runtime type vs static type
  // ---------------------------
  // We can treat any TaxAccount as an account:
  val account: Account = tax

  // However, we now lost information, that TaxAccount has
  // more methods.

  //account.history()

  // Note that we CANNOT use an Account, where a TaxAccount
  // is required:

  // val taxAccount: TaxAccount = account

  // WHY do you think this is the case?


  // Dynamic Dispatch and Subtype Polymorphism
  // =========================================
  class A {
    def method = 1
  }

  class B extends A {
    override def method = 2
  }

  val a1 = new A
  val b1 = new B
  val a2 = new A


  def printValue(a: A) =
    println(a.method)

  printValue(a1)
  printValue(b1)

  // ORIGINAL MOTIVATION:
  // Subtype polymorphism and storing different objects in a list

  val myList: List[A] = List(a1, b1, a2)

  myList.foreach {
    (a: A) => println(a.method)
  }
}


// Scala's traits are *very* powerful.
object demoTraits {

  // 1) Traits can also have implementation
  trait A { def sayA() = println("Aaaaahh") }
  trait B { def sayB() = println("Bee") }

  // 2) You can compose different traits from other traits
  trait AB extends A with B

  // 3) Classes can inherit from / implement multiple traits
  class SayBoth extends A with B
  val both = new SayBoth
  both.sayA()
  both.sayB()

  // 4) Traits can have abstract and concrete fields / methods
  trait Person {

    // abstract field
    val favoriteColor: String

    // abstract method
    def sayName(): Unit

    // concrete field (US)
    val colorSpelling: String = "color"

    // concrete method
    def greet(): Unit =
      sayName()
      println(s"My favorite ${colorSpelling} is: ${favoriteColor}")
  }

  trait Yellow {
    val favoriteColor = "Yellow"
  }

  // Traits can also take constructor arguments
  trait NamedPerson(name: String) {
    def sayName() = println("My name is " + name)
  }

  class YellowPerson(name: String) extends Person with Yellow with NamedPerson(name)
  val jonathan = new YellowPerson("Jonathan")
  jonathan.greet()

  // Instances of YellowPerson are also instances of Person, Yellow, and NamedPerson:
  def greeter(p: NamedPerson) =
    println("Welcome!")
    p.sayName()

  greeter(jonathan)
}
