package OOP.Lior

//- Add a class Participant.kt - each participant has a name (Name), email. Create getter and setter
data class Participant(val name: String, val email: String)

//- Add class Meeting.kt - each meeting need to know name and location
open class Meeting(val name: String, val location: Location)
{
    private val _participants = mutableListOf<Participant>()

    val participants: List<Participant> get() = _participants

    fun addParticipant(participant: Participant):Boolean {
        return this._participants.add(participant)
    }
}

//- There a specific type of a meeting called Personal Review - it has name, participant (single),
//  reviewers (many) and location. When creating a Personal Review return a msg of success
class PersonalReview(name: String, val participant: Participant,val reviewers: List<Reviewer>, location: Location): Meeting(name, location)
{
    init{
        println("Success!\nReview created with name: ${this.name} and location: ${this.location}\n" +
        "There are: ${this.reviewers.size} reviewers! \n" +
        "The participant details are: ${this.participant}")
    }
}

data class Reviewer(val name: String, val email: String)

//- Location can have many types (street, city,county,zip code) and there is a difference between US,
//UK (uk has a postcode instead of zip)
class Location(val street: String,val city: String,val country: Country, val code: String)
{
    override fun toString(): String {
        return "Street: $street, City: $city, Country: $country, ${country.codeType}: $code"
    }
}

enum class Country(val codeType: String){
    ISRAEL("zipcode"),
    US("zipcode"),
    UK("postcode"),
    SPAIN("zipcode"),
    POLAND("zipcode")
}


fun main() {
    //- You can add participants to a meeting
    val participants: List<Participant> = listOf(
        Participant("Johnny","Johnny@hibob.com"),
        Participant("Bob","Johnny@hibob.com"),
        Participant("Jerry","Johnny@hibob.com"),
        Participant("Donny","Johnny@hibob.com"),
        Participant("kidu","Johnny@hibob.com")
    )

    val locationsList: List<Location> = listOf(
        Location("Rabin", "Haifa", Country.ISRAEL, "1234"),
        Location("UsStreet", "New York", Country.US, "2345"),
        Location("UKStreet", "London", Country.UK, "3456")
    )

    val meetings: List<Meeting> = listOf(
        Meeting("Bob story",locationsList[0]),
        Meeting("Kotlin",locationsList[1]),
        Meeting("Scala",locationsList[2])
    )

    //- You can add participants to a meeting
    meetings[0].addParticipant(participants[0])
    println( meetings[0].participants)

    val reviewers = listOf(
        Reviewer("Reviewer 1", "rev1@mail.com"),
        Reviewer("Reviewer 2", "rev2@mail.com")
    )

    val personalReview = PersonalReview("Quarterly Review", participants[1], reviewers, locationsList[0])
}