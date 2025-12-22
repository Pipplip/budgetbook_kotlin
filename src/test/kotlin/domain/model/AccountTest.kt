import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

internal class AccountTest {

    lateinit var account: Account
    lateinit var accountId1: UUID

    lateinit var account2: Account
    lateinit var accountId2: UUID

    lateinit var jsonNormalizer: Json

    @BeforeEach
    fun setUp() {
        accountId1 = UUID.randomUUID()
        account = Account(accountId1, "Tester")

        accountId2 = UUID.randomUUID()
        account2 = Account(accountId2, "Tester_2")

        jsonNormalizer = Json { ignoreUnknownKeys = true }
    }

    @Test
    fun jsonAccountSerialization_test() {
        val json = Json.encodeToString(account)
        // val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")

        val expected = """
            {
                "id":"$accountId1",
                "owner":"Tester"
            }
        """.trimIndent()

        Assertions.assertEquals(
            jsonNormalizer.parseToJsonElement(expected),
            jsonNormalizer.parseToJsonElement(json)
        )
    }

    @Test
    fun multipleAccounts_test() {
        val accountList = listOf<Account>(account, account2)

        val json = Json.encodeToString(accountList)
        // val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")
        //println(json)

        val expected = """
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

        Assertions.assertEquals(
            jsonNormalizer.parseToJsonElement(expected),
            jsonNormalizer.parseToJsonElement(json)
        )
    }

    @Test
    fun addPayments_to_single_account_test() {
        val payId: UUID = UUID.randomUUID()
        val payId2: UUID = UUID.randomUUID()
        val date: LocalDate = LocalDate.now()
        val payment: Payment = Payment(payId, "10.5".toBigDecimal(), date, "payment1")
        val payment2: Payment = Payment(payId2, 20.toBigDecimal(), date, "payment2")

        account.addPayment(payment)
        account.addPayment(payment2)

        val expected = """
            {
                "id":"$accountId1",
                "owner":"Tester",
                "payments":[
                    {
                        "id":"$payId",
                        "amount":"10.5",
                        "date": "2025-12-22",
                        "description":"payment1"
                    },
                    {
                        "id":"$payId2",
                        "amount":"20",
                        "date": "2025-12-22",
                        "description":"payment2"
                    }
                 ]
            }
        """.trimIndent()

        val json = Json.encodeToString(account)
//        println(json)

        Assertions.assertEquals(
            jsonNormalizer.parseToJsonElement(expected),
            jsonNormalizer.parseToJsonElement(json)
        )
    }
}