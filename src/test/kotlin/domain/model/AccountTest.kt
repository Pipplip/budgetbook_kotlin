import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

internal class AccountTest {

    lateinit var account: Account
    lateinit var id: UUID
    lateinit var jsonNormalizer: Json

    @BeforeEach
    fun setUp() {
        id = UUID.randomUUID()
        account = Account(id, "Tester")
        jsonNormalizer = Json { ignoreUnknownKeys = true }
    }

    @Test
    fun jsonAccountSerialization_test() {
        val json = Json.encodeToString(account)
        // val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")

        val expected = """
            {
                "id":"$id",
                "owner":"Tester"
            }
        """.trimIndent()

        Assertions.assertEquals(
            jsonNormalizer.parseToJsonElement(expected),
            jsonNormalizer.parseToJsonElement(json)
        )
    }

    @Test
    fun addPayment_test() {
        val payId: UUID = UUID.randomUUID()
        val amount: BigDecimal = "10.5".toBigDecimal()
        val date: LocalDate = LocalDate.now()
        val payment: Payment = Payment(payId, amount, date, "payment1")

        account.addPayment(payment)

        val expected = """
            {
                "id":"$id",
                "owner":"Tester"
            }
        """.trimIndent()

        val json = Json.encodeToString(account)

        Assertions.assertEquals(
            jsonNormalizer.parseToJsonElement(expected),
            jsonNormalizer.parseToJsonElement(json)
        )

    }
}