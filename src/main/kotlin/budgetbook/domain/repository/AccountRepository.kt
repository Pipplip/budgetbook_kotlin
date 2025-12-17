import de.example.kontoverwaltung.domain.model.Account
import java.util.UUID

interface AccountRepository {
    fun save(account: Account)
    fun findById(id: UUID): Account?
    fun delete(id: UUID)
}