package OOP.Lior

//- Add a class Participant.kt - each participant has a name (Name), email. Create getter and setter
data class Participant(val name: String, val email: String)

//- Add class Meeting.kt - each meeting need to know name and location
open class Meeting(val name: String, val location: Location)
{
    val participants = mutableListOf<Participant>()

    fun addParticipant(participant: Participant):Boolean {
        return this.participants.add(participant)
    }
}

//- There a specific type of a meeting called Personal Review - it has name, participant (single),
//  reviewers (many) and location. When creating a Personal Review return a msg of success
class PersonalReview(name: String, val participant: Participant, reviewers:List<Reviewer>, location: Location): Meeting(name, location)

data class Reviewer(val name: String, val email: String)

//- Location can have many types (street, city,county,zip code) and there is a difference between US,
//UK (uk has a postcode instead of zip)
class Location(val street:String,val city:String,val country: Country, val zipcode:String)

enum class Country(country: String){
    ISRAEL("zipcode"),
    US("zipcode"),
    UK("postcode"),
    SPAIN("zipcode"),
    POLAND("zipcode")
}


fun main() {
    //- You can add participants to a meeting
    val participant:List<Participant> = listOf(
        Participant("Johnny","Johnny@hibob.com"),
        Participant("Bob","Johnny@hibob.com"),
        Participant("Jerry","Johnny@hibob.com"),
        Participant("Donny","Johnny@hibob.com"),
        Participant("kidu","Johnny@hibob.com")
    )

    val locations:List<Location> = listOf(
        Location("Rabin", "Haifa", Country.ISRAEL, "1234"),
        Location("UsStreet", "New York", Country.US, "2345"),
        Location("UKStreet", "London", Country.UK, "3456")
    )

    val meetings:List<Meeting> = listOf(
        Meeting("Bob story",locations[0]),
        Meeting("Kotlin",locations[1]),
        Meeting("Scala",locations[2])
    )

    //- You can add participants to a meeting
    meetings[0].addParticipant(participant[0])
    println( meetings[0].participants)
}