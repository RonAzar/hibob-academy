package com.hibob.academy.nullability

data class Department(val name: String?, val manager: EmployeeDetails?)
data class EmployeeDetails(val name: String?, val contactInfo: ContactInfo?)
data class ContactInfo(val email: String?, val phone: String?)

fun main() {
    val departments = listOf(
        Department("Engineering", EmployeeDetails("Alice", ContactInfo("alice@example.com", null))),
        Department("Human Resources", null),
        Department(null, EmployeeDetails("Bob", ContactInfo(null, "123-456-7890"))),
        Department("Marketing", EmployeeDetails(null, ContactInfo("marketing@example.com", "987-654-3210")))
    )

    // Task: Print each department's name and manager's contact information.
    // If any information is missing, use appropriate defaults.
    departments.printEachDepartmentNameAndContactInfo()
}

fun List<Department?>.printEachDepartmentNameAndContactInfo() {
    this.forEach {
            department -> println("Department name is: " +
            "${department?.name?.let{ "${it}" } ?: "Scooby Doo!"}"
            + " | Employee Details are: name: " + "${department?.manager?.name?.let { "${it}" } ?: "Ron Azar"}"
            + " | Contact info: email: " + "${department?.manager?.contactInfo?.email?.let { "${it}" } ?: "Ron.Azar@hibob.io"}"
            + " | Phone number: " + "${department?.manager?.contactInfo?.phone?.let { "${it}" } ?: "054-9423644"}"
    )}
}