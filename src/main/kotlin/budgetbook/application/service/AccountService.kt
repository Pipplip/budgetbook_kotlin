import java.math.BigDecimal
import java.time.LocalDate
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

    fun getAndInitAllAccounts(): MutableMap<UUID, Account>{
        return repository.getAllAccounts()
    }

    fun addPayment(accountId: UUID, paymentAmount: BigDecimal, description: String, date: LocalDate = LocalDate.now()): Payment {
        val account = repository.findById(accountId)
            ?: throw AccountNotFoundException(accountId)

        val paymentId = UUID.randomUUID()
        val payment = Payment(paymentId, paymentAmount, date, description)
        account.addPayment(payment)
        repository.save(account)

        return payment
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

    fun getBalanceForSpecificMonthYear(accountId: UUID, year: Int, month: Month) : BigDecimal  =
        repository.findById(accountId)?.getBalanceForSpecificMonthYear(year, month)
            ?: throw AccountNotFoundException(accountId)

    fun hasPayments(id: UUID): Boolean {
        val account = repository.getAccountById(id)
        return account != null && account.payments().isNotEmpty()
    }


}