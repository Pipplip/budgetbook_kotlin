import java.util.*
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class InMemoryAccountRepository : AccountRepository {

    private val accounts = mutableMapOf<UUID, Account>()

    override fun save(account: Account) {
        accounts[account.id] = account
    }

    override fun findById(id: UUID): Account? =
        accounts[id]

    override fun delete(id: UUID): Boolean {
        accounts.remove(id) != null
        return true // TODO
    }

    override fun findAll(): List<Account> =
        accounts.values.toList()
}