import java.util.*
import kotlin.collections.mutableMapOf

interface AccountRepository {
    fun save(account: Account)
    fun findById(id: UUID): Account?
    fun delete(id: UUID): Boolean
    fun findAll(): List<Account>
}