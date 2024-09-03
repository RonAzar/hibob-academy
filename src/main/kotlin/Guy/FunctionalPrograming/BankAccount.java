package Guy.FunctionalPrograming;

import java.math.BigDecimal

data class BankAccount(val customerId: Long, val customerType: CustomerType, val balance: BigDecimal)

data class BankAccountWithAnalysis(val bankAccount: BankAccount, val analysis: Analysis)

sealed class CustomerType {
    object Small : CustomerType()
    object Medium : CustomerType()
    object Large : CustomerType()
    object VIP : CustomerType()
}

sealed class Analysis {
    object GoodAnalysis : Analysis()
    object BadAnalysis : Analysis()
    object NoAnalysis : Analysis()
}
