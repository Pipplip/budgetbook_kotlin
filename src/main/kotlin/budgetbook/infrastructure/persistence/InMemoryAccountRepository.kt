import java.util.*

class InMemoryAccountRepository : AccountRepository {

    private val accounts = mutableMapOf<UUID, Account>()

    override fun save(account: Account) {
        accounts[account.id] = account
    }

    override fun findById(id: UUID): Account? =
        accounts[id]

    override fun delete(id: UUID) {
        accounts.remove(id)
    }
}