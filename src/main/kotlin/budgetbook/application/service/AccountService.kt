import de.example.kontoverwaltung.domain.exception.AccountNotFoundException
import de.example.kontoverwaltung.domain.model.Account
import de.example.kontoverwaltung.domain.model.Payment
import de.example.kontoverwaltung.domain.repository.AccountRepository
import java.util.UUID

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
}