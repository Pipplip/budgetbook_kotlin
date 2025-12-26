fun main() {
//    val repository = JsonAccountRepository()
//    if (repository is PersistableRepository) {
//        repository.loadFromFile()
//    }

    val repository = InMemoryAccountRepository()
    val service = AccountService(repository)
    val app = ConsoleApp(service)

    app.run()

//    if (repository is PersistableRepository) {
//        // Optional: Persist nach Ende
//        repository.persistAllToFile()
//    }
}