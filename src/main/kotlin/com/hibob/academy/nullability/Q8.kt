package com.hibob.academy.nullability
/**
* Instructions:
*
* Traverse through the company structure starting from departments to teams and finally to team members.
* For each level (company, department, team, leader, members), check for null values and print appropriate information.
* Ensure that every piece of information printed includes a fallback for null values using the Elvis operator and that
* blocks of code dependent on non-null values are executed using ?.let.
*
*/
data class Company(val name: String?, val departments: List<DepartmentDetails?>?)
data class DepartmentDetails(val name: String?, val teams: List<Team?>?)
data class Team(val name: String?, val leader: Leader?, val members: List<Member?>?)
data class Leader(val name: String?, val title: String?)
data class Member(val name: String?, val role: String?)

fun initializeCompany(): Company {
    return Company(
        "Tech Innovations Inc.",
        listOf(
            DepartmentDetails("Engineering", listOf(
                Team("Development", Leader("Alice Johnson", "Senior Engineer"), listOf(Member("Bob Smith", "Developer"), null)),
                Team("QA", Leader(null, "Head of QA"), listOf(Member(null, "QA Analyst"), Member("Eve Davis", null))),
                null
            )),
            DepartmentDetails(null, listOf(
                Team("Operations", null, listOf(Member("John Doe", "Operator"), Member("Jane Roe", "Supervisor")))
            )),
            null
        )
    )
}

fun main9() {
    val company = initializeCompany()

    // Task: Print detailed information about each department, team, and team members, handling all null values appropriately.
    printCompanyDetails(company)
}

fun printCompanyDetails(company: Company) {
    company?.let {
        val companyName = it.name ?: "Unknown Company name"
        print("| Company Name: $companyName Company information:")
        printDepartmentsDetails(it.departments)
        println()
    }?: println("| Unknown company |")
}


fun printDepartmentsDetails(departments: List<DepartmentDetails?>?){
    departments?.let { it.forEach{ department->
        department?.let {
            val departmentName = department.name ?: "Unknown Department name"
            println("| Department Name: $departmentName Department information: ")
            printTeamsDetails(department.teams)
        }?:println("| Unknown Department |")
    }?:print("| Unknown Departments |")
}
}

fun printTeamsDetails(teams: List<Team?>?){
    teams?.let {
        it.forEach{ team->
            team?.let {
                val teamName = team.name ?: "Unknown Team name"
                println("§ Team name: $teamName , Info about the team:")
                printLeaderDetails(team.leader)
                printMembersDetails(team.members)
            }?: println("§ Unknown Team §")
        }?:print("| Unknown Teams |")
    }
}

fun printLeaderDetails(leader: Leader?){
    leader?.let {
        val leaderName = leader.name ?: "Unknown Leader Name"
        val leaderTitle = leader.title ?: "Unknown Leader Title"
        println("± Leader name: $leaderName & Leader Title: $leaderTitle ±")
    }?: println("± Unknown Leader ±")
}

fun printMembersDetails(members: List<Member?>?){
    members?.let {
        it.forEach{ member->
            member?.let {
                val memberName = member.name ?: "Unknown Member name"
                val memberRole = member.role ?: "Unknown Member role"
                println("# member Role: $memberName & member Role: $memberRole #")
            }?: println("# Unknown Member #")
        }?:print("| Unknown Members |")
    }
}








