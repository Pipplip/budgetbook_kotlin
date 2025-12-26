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

    fun deleteAccount(accountId: UUID): Boolean =
        repository.delete(accountId)

    fun getAllAccounts(): List<Account> =
        repository.findAll()

    fun addPayment(accountId: UUID, amount: BigDecimal, description: String, date: LocalDate = LocalDate.now()): Payment {
        val account = repository.findById(accountId)
            ?: throw AccountNotFoundException(accountId)

        val payment = Payment(amount = amount, date = date, description = description)
        account.addPayment(payment)
        repository.save(account)

        return payment
    }

    fun removePayment(accountId: UUID, paymentId: UUID) {
        val account = repository.findById(accountId)
            ?: throw AccountNotFoundException(accountId)

        if (!account.removePayment(paymentId)) {
            throw PaymentNotFoundException(paymentId)
        }

        repository.save(account)
    }

    fun getBalance(accountId: UUID) =
        repository.findById(accountId)?.balance()
            ?: throw AccountNotFoundException(accountId)

    fun getBalanceFor(accountId: UUID, year: Int, month: Month) : BigDecimal  =
        repository.findById(accountId)?.balanceFor(year, month)
            ?: throw AccountNotFoundException(accountId)

    fun hasPayments(id: UUID): Boolean {
        val account = repository.findById(id)
        return account != null && account.getPayments().isNotEmpty()
    }

    fun saveAll(): Boolean  {
        if (repository is PersistableRepository) {
            return repository.persistAllToFile()
        }
        return true
    }
}