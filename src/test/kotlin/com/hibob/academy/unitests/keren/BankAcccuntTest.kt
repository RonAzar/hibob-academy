import com.hibob.bootcamp.BankAccount
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Write unit tests to verify that the deposit and withdraw methods function correctly.
 * Handle edge cases, such as invalid inputs (e.g., negative amounts).
 * Ensure that the getBalance method returns the correct balance after a series of deposits and withdrawals.
 */

class BankAccountTest {


    @Test
    fun `deposit valid amount increases balance`() {
        assertEquals(100.0,BankAccount(0.0).deposit(100.toDouble()))
    }

    @Test
    fun `deposit negative or zero amount throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            BankAccount(0.0).deposit(0.0)
        }
    }

    @Test
    fun `withdraw valid amount decreases balance`() {
        assertEquals(0.0,BankAccount(1.0).withdraw(1.0))
    }

    @Test
    fun `withdraw amount greater than balance throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            BankAccount(0.0).withdraw(1.0)
        }
    }

    @Test
    fun `withdraw negative or zero amount throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            BankAccount(1.0).withdraw(-1.0)
        }
    }

    @Test
    fun `getBalance returns the correct balance`() {
        assertEquals(100.00,BankAccount(100.0).getBalance())
    }
}
