package NullAbilityUpdated.Keren

data class DepartmentData(val name: String?, val manager: EmployeeData?)
data class EmployeeData(val name: String?, val contactInfo: Contact?)
data class Contact(val email: String?, val phone: String?)

fun main() {
    val departments = listOf(
        DepartmentData("Engineering", EmployeeData("Alice", Contact("alice@example.com", "123-456-7890"))),
        DepartmentData("Human Resources", null),
        DepartmentData("Operations", EmployeeData("Bob", Contact(null, "234-567-8901"))),
        DepartmentData("Marketing", EmployeeData(null, Contact("marketing@example.com", "345-678-9012"))),
        DepartmentData("Finance", EmployeeData("Carol", Contact("", "456-789-0123")))
    )

    // Implement the features here.
//    * Filter Departments: Identify departments that have either no manager assigned or where the manager's
//    * contact information is entirely missing.
    val filterDepartmentsList = filterDepartments(departments)
    println("Filtered departments: $filterDepartmentsList")

//    * Email List Compilation: Generate a list of all unique manager emails but exclude any null
//    * or empty strings. Ensure the list has no duplicates.
    val emailsList = makeManagersEmailsList(filterDepartmentsList)
    println("emails: $emailsList")

//    * Reporting: For each department, generate a detailed report that includes the
//    * department name, manager's name, email, and formatted phone number.
//    * If any information is missing, provide a placeholder.
    printsReport(departments)
}

fun filterDepartments(departments: List<DepartmentData>): List<DepartmentData> {
    return departments.filter { department ->
            department.manager?.let { department.manager.contactInfo?.let { true }?:false } ?: false
    }
}

fun makeManagersEmailsList(departments: List<DepartmentData>):List<String>{
    return departments.mapNotNull { department->
        department.manager?.contactInfo?.email?.let { email -> email}
    }.distinct()
}

//    * Reporting: For each department, generate a detailed report that includes the
//    * department name, manager's name, email, and formatted phone number.
//    * If any information is missing, provide a placeholder.
fun  printsReport(departments: List<DepartmentData>){
    departments.forEach { department->
        val departmentName = department.name ?: "Unknown Department Name!"
        val departmentManagerName = department.manager?.name ?: "Unknown Department Manager Name!"
        val departmentManagerEmail = department.manager?.contactInfo?.email ?: "Unknown Department Manager Email!"
        val departmentManagerPhoneNumber = formatPhoneNumber(department.manager?.contactInfo?.phone ?: "")
        printReportFormat(departmentName, departmentManagerName, departmentManagerEmail, departmentManagerPhoneNumber)
    }
}

fun printReportFormat(departmentName: String, departmentManagerName: String, departmentManagerEmail: String, departmentManagerPhoneNumber: String) {
    println("±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±"+
    "\n|\t Department Name: $departmentName"+
    "\n|\t Department Manager Name: $departmentManagerName"+
    "\n|\t Department Manager Email: $departmentManagerEmail"+
    "\n|\t Department Manager Phone: $departmentManagerPhoneNumber"+
   "\n±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±")
}

fun  formatPhoneNumber(phoneNumber: String): String{
val cleanPhoneNumber = phoneNumber.replace("-", "")
    if (cleanPhoneNumber.length == 10) {
        return String.format(
            "(+%s) %s-%s",
            cleanPhoneNumber.substring(0, 3),
            cleanPhoneNumber.substring(3, 6),
            cleanPhoneNumber.substring(6, 10)
        )
    }
    return "Unknown Department Manager Phone!"
}
