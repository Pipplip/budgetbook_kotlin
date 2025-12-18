import java.time.Month
import java.util.*
import kotlin.collections.mutableMapOf

class AccountService(
    private val repository: AccountRepository
) {

    fun createAccount(owner: String): Account {
        val account = Account(owner = owner)
        repository.save(account)
        return account
    }

    fun deleteAccount(accountId: UUID) {
        repository.delete(accountId)
    }

    fun getAllAccounts(): MutableMap<UUID, Account>{
        return repository.getAllAccounts()
    }

    fun addPayment(accountId: UUID, payment: Payment) {
        val account = repository.findById(accountId)
            ?: throw AccountNotFoundException(accountId)

        account.addPayment(payment)
        repository.save(account)
    }

    fun removePayment(accountId: UUID, paymentId: UUID) {
        val account = repository.findById(accountId)
            ?: throw AccountNotFoundException(accountId)

        account.removePayment(paymentId)
        repository.save(account)
    }

    fun getBalance(accountId: UUID) =
        repository.findById(accountId)?.balance()
            ?: throw AccountNotFoundException(accountId)

    fun getBalanceForSpecificMonthYear(accountId: UUID, year: Int, month: Month) =
        repository.findById(accountId)?.getBalanceForSpecificMonthYear(year, month)
            ?: throw AccountNotFoundException(accountId)
}