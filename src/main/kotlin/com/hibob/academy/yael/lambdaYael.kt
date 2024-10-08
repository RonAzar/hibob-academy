package com.hibob.academy.yael

import java.time.LocalDate

fun main13() {
    //2. Initiate the variable movie in line 18 with the function createGoodMovie()
    val movie = createGoodMovie()
    val runner = Runner(movie)
    val success = runner.init()

    printSuccessMessage(success)
    println("Pretty print: ${movie.prettyPrint()}")
//    println("Json: ${movie.toJson()}")
}

//2. Initiate the variable movie in line 18 with the function createGoodMovie()
val createGoodMovie :() -> SpidermanNoWayHome = { SpidermanNoWayHome() }

fun printSuccessMessage(success: Boolean) {
    if (success) {
        println("The movie was successful")
    } else {
        println("The movie failed")
    }
}

interface SpidermanMovieProduceActions {
    fun signTobeyMaguire()
    fun signAndrew()
    fun signTom()
    fun getVillains()
    fun isThereLockdown(): Boolean
    fun publish():Boolean

    val title: String
    val airDate: LocalDate
    val imdbRank: Double
}

class SpidermanNoWayHome() : SpidermanMovieProduceActions {
    override val title: String = "Spiderman - No Way Home"
    override val airDate: LocalDate = LocalDate.of(2021,12,16)
    override val imdbRank: Double = 9.6

    //3. Implement pretty print using buildString function
    fun prettyPrint(): String {
        //            Title: title
        //            Air date: airDate
        //            IMDB rank: rank
        //            use buildString function!
        //        return "pretty print was not implemented yet"
        val builder:String = buildString {
            appendLine("Title: $title")
            appendLine("Air Date: $airDate")
            appendLine("Imdb Rank: $imdbRank")
        }
        return builder
    }
    override fun signTobeyMaguire() {
        println("Tobey signed!")
    }

    override fun signAndrew() {
        println("Andrew signed!")
    }

    override fun signTom() {
        println("Tom signed")
    }

    override fun getVillains()
    {
        val villains = listOf(
            "Green Goblin",
            "Doctor Octopus",
            "Sandman",
            "Venom",
            "The Lizard",
            "Electro",
            "Vulture",
            "Mysterio"
        ).forEach(::println)
    }

    override fun isThereLockdown(): Boolean = false

    override fun publish(): Boolean = true

//    fun toJson(): JsonNode {
//        /* implement the following json structure:
//                {
//                 "title" : title,
//                 "airDate": 2021-12-16,
//                 "imdbRank": 9.6
//                }
//
//        Note: In kotlin we receive the default object serializer "for free"
//        and we will not have to implement it from here
//        But, knowing how to write jsons in kotlin is still very important!
//        the common use cases: S2S clients implementation, tests and more */
//
//        return JsonBuilderObject().json {
//            "notImplemented" - true
//        }.asTree()
//    }

}

fun buildString(actions: StringBuilder.() -> Unit):String{
    val builder = StringBuilder()
    builder.actions()
    return builder.toString()
}

//1. Make the Runner.init function more readable using Lambda with Receiver function usage
//4. Make SpidermanMovieProduceActions nullable (if not so yet) and make the relevant adjustments
class Runner(private val movieProducer: SpidermanMovieProduceActions?){
    fun init(): Boolean{
        return movieProducer?.run{
                if(!isThereLockdown()){
                signTobeyMaguire()
                signAndrew()
                signTom()
                getVillains()
                publish()
            }else false
        }?:false
    }
}