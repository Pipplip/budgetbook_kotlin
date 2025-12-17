import application.service.AccountService
import infrastructure.repository.InMemoryAccountRepository
import ui.cli.ConsoleApp

fun main() {
    val repository = InMemoryAccountRepository()
    val service = AccountService(repository)
    val app = ConsoleApp(service)

    app.run()
}