import java.util.*
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class InMemoryAccountRepository : AccountRepository {

    private val accounts = mutableMapOf<UUID, Account>()

    override fun save(account: Account) {
        accounts[account.id] = account
    }

    override fun saveAll() {
    }

    override fun findById(id: UUID): Account? =
        accounts[id]

    override fun delete(id: UUID) {
        accounts.remove(id)
    }

    override fun getAllAccounts(): MutableMap<UUID, Account> {
        return accounts
    }

    override fun getAccountById(id: UUID): Account? {
        return accounts[id]
    }
}