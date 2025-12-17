fun main() {
    val repository = InMemoryAccountRepository()
    val service = AccountService(repository)
    val app = ConsoleApp(service)

    app.run()
}