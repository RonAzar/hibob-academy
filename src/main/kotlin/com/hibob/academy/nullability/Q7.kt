package com.hibob.academy.nullability

/**
 *
 * Iterate through the list of customers, which may contain null entries.
 * For each non-null customer, print the customer's name or "Name Unknown" if the name is null.
 * Print the account ID or "Account ID Unknown" if the account or ID is null.
 * Print the account balance or "Balance Not Available" if the account details or balance is null.
 * If the customer object itself is null, print "Customer data is not available."
 *
 */
data class Customer(val name: String?, val account: Account?)
data class Account(val id: String?, val details: AccountDetails?)
data class AccountDetails(val type: String?, val balance: Double?)

fun initializeNullableCustomers(): List<Customer?> {
    return listOf(
        Customer("John Doe", Account("12345", AccountDetails("Checking", 1500.00))),
        null,
        Customer("Jane Smith", Account("67890", AccountDetails(null, 780.00))),
        Customer(null, Account(null, AccountDetails("Savings", null))),
        null,
        Customer("Emily White", null)
    )
}

fun main8() {
    val customers = initializeNullableCustomers()

    // Task: Print each customer's name, account ID, and account balance. Handle all null cases appropriately.
    customers.printAllCustomersDetails()
}

fun List<Customer?>.printAllCustomersDetails(){
    this.forEach {costumer->
        var costumerName = "Unknown name"
        var accountId = "Unknown Id"
        var accountType = "Unknown Type"
        var accountBalance = 0.00
        costumer?.let {
            costumer.name?.let { costumerNm -> costumerName = costumerNm  }
            costumer.account?.let { costumerAccount ->
                costumerAccount.id?.let { costumerId -> accountId = costumerId }
                costumerAccount.details?.let { costumerDetails ->
                    costumerDetails.type?.let { costumerType -> accountType=costumerType }
                    costumerDetails.balance?.let { costumerBalance-> accountBalance= costumerBalance }
                }
            }
        }
        println(Customer(costumerName, Account(accountId, AccountDetails(accountType,accountBalance))))
    }
}
