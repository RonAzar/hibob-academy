package Guy.FunctionalPrograming

import javax.inject.Singleton

@Singleton
class BankAccountHandler {

    /*
        This method goes over all bank accounts.
        If the balance is positive it considers a good account, if negative it's bad and if zero it's
        for those with no Bad analysts, it promotes their type and also returns the account with the new type and its analysis
    */
    fun handleAccounts(bankAccounts: List<BankAccount>): List<BankAccountWithAnalysis> {
        return giveAnalysis(bankAccounts)
    }

    // Functional programming style for the giveAnalysis function
    private fun giveAnalysis(bankAccounts: List<BankAccount>): List<BankAccountWithAnalysis> {
        return bankAccounts.map { account ->
            when {
                account.balance > 0 -> {
                    val newBalance = account.balance * BigDecimal("1.01")
                    val updatedCustomerType = upgrade(account)
                    BankAccountWithAnalysis(account.copy(balance = newBalance, customerType = updatedCustomerType), GoodAnalysis)
                }
                account.balance < 0 -> {
                    val updatedCustomerType = downgrade(account)
                    BankAccountWithAnalysis(account.copy(customerType = updatedCustomerType), BadAnalysis)
                }
                else -> BankAccountWithAnalysis(account, NoAnalysis)
            }
        }
    }

    private fun upgrade(bankAccount: BankAccount): CustomerType {
        return when (bankAccount.customerType) {
            CustomerType.Small -> CustomerType.Medium
            CustomerType.Medium -> CustomerType.Large
            CustomerType.Large -> CustomerType.VIP
            CustomerType.VIP -> CustomerType.VIP
        }
    }

    private fun downgrade(bankAccount: BankAccount): CustomerType {
        return when (bankAccount.customerType) {
            CustomerType.Small -> CustomerType.Small
            CustomerType.Medium -> CustomerType.Small
            CustomerType.Large -> CustomerType.Medium
            CustomerType.VIP -> CustomerType.Large
        }
    }
}
