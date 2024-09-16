package sequence.omri
import org.springframework.cglib.core.Predicate
import kotlin.system.exitProcess

fun main14(args: Array<String>) {
    ex1()
    ex2()
    ex3()
    ex4()
    ex5()
}

fun ex1() {
    // change the program to
    // 1. reuse the filter / map function
    // 2. println each call to track the diffs between List and Seq

    val list = listOf(1, 2, 3, 4)

    val maxOddSquare = list
        .map {println("Square: $it")
            square(it) }
        .filter { println("Is even: $it")
            isEven(it) }
        .find { println("Check if $it equal to four")
            equalToFour(it) }

    val maxOddSquare2 = list.asSequence()
        .map {println("Square: $it")
            square(it) }
        .filter { println("Is even: $it")
            isEven(it) }
        .find { println("Check if $it equal to four")
            equalToFour(it) }
}

fun isEven(x: Int) = x % 2 == 0
fun square(n: Int) = n * n
fun equalToFour(n: Int) = n == 4


fun ex2() {
    // how many times filterFunc was called
    fun filterFunc(it: Int): Boolean {
        println("filterFunc was called")
        return it < 3
    }
    sequenceOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter { filterFunc(it) }
    //Foreach is not included so its: 0
}

fun ex3() {
    // create the list of the first 10 items of (2, 4, 8, 16, 32 ...) seq
    val twoSq = generateSequence(2){ i: Int -> i * 2 }.take(10).forEach(::println)
}

fun ex4() {
    // create the list of the first 10 items of the Fibonacci seq
    val fibonacci = generateSequence(Pair(0,1)) { Pair(it.second,(it.first+it.second)) }.map { it.first }.take(10).forEach {
        print("$it ")
    }
}

fun ex5() {
    // try to minimize the number of operations:
    val engToHeb: Map<String, String> = mapOf("today" to "היום",
        "was" to "היה",
        "good" to "טוב",
        "day" to "יום",
        "for" to "בשביל",
        "walking" to "ללכת",
        "in" to "ב",
        "the" to "ה",
        "park" to "פארק",
        "sun" to "שמש",
        "was" to "הייתה",
        "shining" to "זורחת",
        "and" to "ו",
        "birds" to "ציפורים",
        "were" to "היו",
        "chirping" to "מצייצות") // assume the dictionary is real :-)
    val b = "today was a good day for walking in the part. Sun was shining and birds were chirping"
        .splitToSequence(" ")
        .mapNotNull {engToHeb[it]}
        .filter {it.length <= 3 }
        .take(5)
        .count() > 4
}