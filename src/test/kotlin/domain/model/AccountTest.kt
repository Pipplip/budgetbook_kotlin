import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

internal class AccountTest {

    lateinit var account1: Account
    lateinit var accountId1: UUID

    lateinit var account2: Account
    lateinit var accountId2: UUID

    lateinit var jsonNormalizer: Json

    @BeforeEach
    fun setUp() {
        accountId1 = UUID.randomUUID()
        account1 = Account(accountId1, "Tester")

        accountId2 = UUID.randomUUID()
        account2 = Account(accountId2, "Tester_2")

        jsonNormalizer = Json { ignoreUnknownKeys = true }
    }

    @Test
    fun jsonAccountSerialization_test() {
        val actualJson = Json.encodeToString(account1)
        // val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")

        val expectedJson  = """
            {
                "id":"$accountId1",
                "owner":"Tester"
            }
        """.trimIndent()

        assertJsonEquals(expectedJson, actualJson)
    }

    @Test
    fun multipleAccounts_test() {
        val accountList = listOf<Account>(account1, account2)

        val actualJson = Json.encodeToString(accountList)
        // val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")
        //println(actualJson)

        val expectedJson = """
            [
              {
                "id":"$accountId1",
                "owner":"Tester"
              },
              {
                "id":"$accountId2",
                "owner":"Tester_2"
              }
            ]
        """.trimIndent()

        assertJsonEquals(expectedJson, actualJson)
    }

    @Test
    fun addPayments_to_single_account_test() {
        val payId: UUID = UUID.randomUUID()
        val payId2: UUID = UUID.randomUUID()
        val date = LocalDate.of(2025, 12, 22)
        val payment = Payment(payId, "10.5".toBigDecimal(), date, "payment1")
        val payment2 = Payment(payId2, 20.toBigDecimal(), date, "payment2")

        account1.addPayment(payment)
        account1.addPayment(payment2)

        val expectedJson = """
            {
                "id":"$accountId1",
                "owner":"Tester",
                "payments":[
                    {
                        "id":"$payId",
                        "amount":"10.5",
                        "date": "$date",
                        "description":"payment1"
                    },
                    {
                        "id":"$payId2",
                        "amount":"20",
                        "date": "$date",
                        "description":"payment2"
                    }
                 ]
            }
        """.trimIndent()

        val actualJson = Json.encodeToString(account1)
//        println(actualJson)

        assertJsonEquals(expectedJson, actualJson)
    }

    @Test
    fun addPayment(){
        val payment = Payment(UUID.randomUUID(), "10.5".toBigDecimal(), LocalDate.of(2025, 12, 22), "payment1")
        account1.addPayment(payment)
        Assertions.assertEquals(1, account1.payments().size)
    }

    private fun assertJsonEquals(expected: String, actual: String) {
        Assertions.assertEquals(
            jsonNormalizer.parseToJsonElement(expected),
            jsonNormalizer.parseToJsonElement(actual)
        )
    }
}