import java.util.UUID
import kotlin.collections.mutableMapOf

class JsonAccountRepository : AccountRepository {

    private val accounts = mutableMapOf<UUID, Account>()

    override fun save(account: Account) {
        //TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Account? {
        //TODO("Not yet implemented")
        return null
    }

    override fun delete(id: UUID) {
        //TODO("Not yet implemented")
    }

    override fun getAllAccounts(): MutableMap<UUID, Account> {
        //TODO("Not yet implemented")
        return accounts
    }

}