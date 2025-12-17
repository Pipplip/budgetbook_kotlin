import java.math.BigDecimal

class ConsoleApp(
    private val service: AccountService
) {

    fun run() {
        val account = service.createAccount("Max Mustermann")
        println("Konto erstellt: ${account.id}")

        val payment = Payment(
            amount = BigDecimal("100.00"),
            description = "Gehalt"
        )

        service.addPayment(account.id, payment)

        println("Saldo: ${service.getBalance(account.id)} €")
    }
}