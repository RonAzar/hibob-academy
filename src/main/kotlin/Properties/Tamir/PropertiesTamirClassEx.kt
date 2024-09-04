package Properties.Tamir
import java.time.DayOfWeek

//1. create class called Store that initlize by day and list of products
data class Store(val day: DayOfWeek,val products: List<Product>)
{
    //2. add property to that indicate if the store is open the store is open all the day expect saturday
    val shomeretShabat:Boolean get() {
        when(day){
            DayOfWeek.SATURDAY -> return false
            else -> return true
        }
    }

    //3. add property to that indicate number of product
    val numberOfProducts:Int get() {
        return products.size
    }

    //5. I want to count number of calling get receipts
    var countGetReceipts:Int = 0

    //4. add val that get receipts //no need to implement the method but its an heavy task
    val getReceipts:List<String> by lazy {
        countGetReceipts++
        listOf("Hello","Bob")
    }

    //6. write vairable that initilize only when calling create method
    lateinit var lazyValue:String
    fun initializeVariable(){
        lazyValue="Ron Azar"
    }

}

data class Product(val productName:String)
