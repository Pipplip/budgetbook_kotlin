fun main() {
//    val repository = JsonAccountRepository()
    val repository = InMemoryAccountRepository()
    val service = AccountService(repository)
    val app = ConsoleApp(service)

    service.getAndInitAllAccounts()

    println("==== Willkommen zum BudgetBook CLI Tool ====")
    app.run()
}