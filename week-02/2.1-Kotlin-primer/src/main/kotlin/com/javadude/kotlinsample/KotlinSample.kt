package com.javadude.kotlinsample

fun interface OnClickListener {
    fun onClick(button: Button1)
}
object NullOnClickListener: OnClickListener {
    override fun onClick(button: Button1) {
        button.doSomething()?.length
    }
}

fun Button1.doSomething(): String? {
    // do something
    println(this)
    return "Scott"
}

class Button1 {
    var onClickListener: OnClickListener? = null

    private fun ifIKnowThatIAmClicked() {
        // user touches inside me, and releases inside me
        val staticOnClickListener = onClickListener
        if (staticOnClickListener != null) {
            staticOnClickListener.onClick(this)
        }

        onClickListener?.let { listener ->
            println("Calling the click listener")
            listener.onClick(this)
        }

        onClickListener?.onClick(this)
    }

    var onClickListener2: OnClickListener = NullOnClickListener
    private fun ifIKnowThatIAmClicked2() {
        // user touches inside me, and releases inside me
        onClickListener2.onClick(this)
    }
}

class Button2 {
    private var clickedTimes = 0
    var onClickListener3: ((whichButton: Button2, clickedTimes: Int) -> Unit)? = null
    private fun ifIKnowThatIAmClicked3() {
        clickedTimes++
        // user touches inside me, and releases inside me
        onClickListener3?.invoke(this, clickedTimes)
    }

    var onClickListener: ((Button2) -> Unit)? = null

    private fun ifIKnowThatIAmClicked() {
        // user touches inside me, and releases inside me
        val staticOnClickListener = onClickListener
        if (staticOnClickListener != null) {
            staticOnClickListener(this)
        }

        onClickListener?.invoke(this)
    }

    var onClickListener2: (Button2) -> Unit = { }
    private fun ifIKnowThatIAmClicked2() {
        // user touches inside me, and releases inside me
        onClickListener2(this)
    }
}

class Sample2(
    val x: Int = 0,
    val y: Int = 0
) {
    var z1: Int = 10
    var z2 = 10

    fun foo1(a: Int): Unit {
        val x = if (a < 10) 42 else 100
//      LIKE JAVA:  int x = a < 10 ? 42 : 100

        if (a < 10) {
            println(a)
        } else {
            println("10 or more")
        }
        when (a) {
            1 -> println("a")
            2 -> {
                println("b")
                println("b")
                println("b")
                println("b")
            }
            3 -> println("c")
            4 -> println("d")
            else -> println("xxxxx")
        }
    }
    fun foo2(a: Int): Int {
        return a + 10
    }
    fun foo2a(a: Int) = when(a) {
        1 -> "A"
        2 -> {
            println("hello")
            // calculations
            "B"
        }
        3 -> "C"
        4 -> "D"
        else -> "XXXXXX"
    }
    fun foo3(a: Int): Int = a + 10
    fun foo4(a: Int) = a + 10
}

open class Sample {
    val x: Int = 0 // IMMUTABLE
        get() {
            return field
        }
    var y: Int = 0 // MUTABLE
        get() {
            // log
            return field
        }
        set(value) {
            // log
            field = value
            // invalidate()
        }

    val z1: Int
        get() = x + y
    val z3: Int get() = x + y
    val z2: Int = x + y

    fun foo() {
        println("foo!!!")
    }
}

class SampleSubclass: Sample() {

}

class PersonWithNoConstructor {
    var name: String = ""
    var age: Int = 0
}
data class Person(
    var name: String,
    var age: Int
)
data class PersonWithNulls(
    var name: String?,
    var age: Int
)

class MyOnClickListener1: OnClickListener {
    override fun onClick(button: Button1) {
        println("Pressed!")
    }
}
fun main() {
    val person42 = PersonWithNoConstructor().apply {
        name = "Scott"
        age = 54
    }

    val button1 = Button1()
    button1.onClickListener = MyOnClickListener1()
    button1.onClickListener2 = MyOnClickListener1()
    button1.onClickListener = object: OnClickListener {
        override fun onClick(button: Button1) {
            println("Anonymous Pressed!")
        }
    }
    button1.onClickListener = OnClickListener {
    }

    val button2 = Button2()
    button2.onClickListener = {
        println("Anonymous Pressed: $it!")
    }
    button2.onClickListener2 = { _ ->
        println("Anonymous Pressed!")
    }
    button2.onClickListener2 = { button ->
        println("Anonymous Pressed: $button!")
    }
    button2.onClickListener3 = { button, clickedTimes ->
        println("Anonymous Pressed: $button was clicked $clickedTimes times!")
    }


//  WON'T WORK  val personWithoutNulls = Person(null, 54)
    val personWithoutNulls = Person("Scott", 54)
    val personWithNulls = PersonWithNulls(null, 54)

    personWithoutNulls.name.let { name ->
        println(name.length)
        println(name.hashCode())
    }
    println(personWithoutNulls.name.length)
    println(personWithNulls.name!!.length) // do this VERY rarely
    println(personWithNulls.name?.length ?: 0)
    println(personWithNulls.name?.length ?: throw RuntimeException("name should not be null here"))

    // EXCEPTIONS IN KOTLIN ARE ALL NON-CHECKED

    val x1 = 42
    val x2 = "Hello"
    val x3 = Person("Scott", 54)

    val message = "x1 is $x1, x2 is '$x2', the person name is ${x3.name}, this is the ${x1}nth time we've done this"
    val lines = """
        line 1
        line 2
            line 3
        line 4
    """.trimIndent()
    val lines2 = """
        |line 1
        |line 2
        |    line 3
        |line 4
    """.trimMargin()


    val sample2 = Sample2(x = 10, y = 20)
    val sample3 = Sample2(y = 10, x = 20)
    val sample4 = Sample2(y = 10)
    val sample5 = Sample2(x = 20)

    val sample = Sample()
    println(sample.x)
    println(sample.y)
    sample.y = 0


    val person = Person("Scott", 54)

    println(person.name)
    println(person.age)
    println(person)

    person.name = "Stephanie"
    person.age = 0
    println(person.name)
    println(person.age)
    println(person)
}