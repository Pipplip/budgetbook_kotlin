import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import kotlin.collections.mutableMapOf


@Serializable
data class AccountWrapper(
    val accounts: List<Account>
)

// Bei Programmstart alle Daten in den Speicher von Json laden
// Arbeiten, ändern, hinzufügen etc. passiert nur im Speicher
// Änderungen werden im Speicher vorgenommen und dann explizit persistiert
class JsonAccountRepository(
    private val filePath: String = "accounts.json"
) : AccountRepository, PersistableRepository {

    private val accounts = mutableMapOf<UUID, Account>()

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        serializersModule = SerializersModule {
            contextual(UUID::class, UUIDSerializer)
            contextual(BigDecimal::class, BigDecimalSerializer)
            contextual(LocalDate::class, LocalDateSerializer)
        }
    }

    // ---------------- Repository Interface ----------------
    override fun save(account: Account) {
        accounts[account.id] = account
    }

    override fun findById(id: UUID): Account? {
        return accounts[id]
    }

    override fun delete(id: UUID): Boolean {
        accounts.remove(id) != null
        return true // TODO
    }

    override fun findAll(): List<Account> =
        accounts.values.toList()

    // ---------------- JSON Persistenz ----------------

    override fun persistAllToFile(): Boolean {
        try {
            val wrapper = AccountWrapper(accounts.values.toList())
            val jsonString = json.encodeToString(wrapper)
            val file = File(filePath)
            file.writeText(jsonString)
            println("${file.absolutePath}")
        }catch (e: Exception){
            println(e)
            return false
        }
        return true
    }

    override fun loadFromFile() {
        val file = File(filePath)
        if (!file.exists()) return

        val content = file.readText()
        val wrapper = json.decodeFromString<AccountWrapper>(content)
        accounts.clear()
        wrapper.accounts.forEach { accounts[it.id] = it }
    }

}