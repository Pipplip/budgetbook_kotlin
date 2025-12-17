import java.util.*

interface AccountRepository {
    fun save(account: Account)
    fun findById(id: UUID): Account?
    fun delete(id: UUID)
}