import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class AccountServiceTest {

    // Mock the AccountRepository
    val repo = mockk<AccountRepository>()
    val randomUuid:UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        // set mock data
        every { repo.findAll() }.returns(listOf(Account(owner = "Test Owner")))
        every { repo.save(any<Account>()) } returns Unit
        every { repo.findById(any()) } returns Account(owner = "Special Owner")
        every { repo.delete(randomUuid) } returns true
    }

    @Test
    fun `should create account successfully`() {
        val service = AccountService(repo)
        val account = service.createAccount("New Owner")
        Assertions.assertEquals("New Owner", account.owner)
    }

    @Test
    fun getAllAccounts() {
        val service = AccountService(repo)
        val accounts = service.getAllAccounts()

        Assertions.assertEquals(1, accounts.size)
    }

    @Test
    fun deleteAccount() {
        val service = AccountService(repo)
        val result = service.deleteAccount(randomUuid)

        Assertions.assertTrue(result)
    }

}