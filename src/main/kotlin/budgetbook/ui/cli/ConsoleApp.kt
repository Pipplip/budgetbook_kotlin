import java.math.BigDecimal
import java.util.UUID

class ConsoleApp(
    private val service: AccountService
) {

    var state: AppState = AppState.MainMenu

    fun run() {

        while (state != AppState.Exit) {

            val currentState = state

            state = when (currentState) {
                is AppState.MainMenu -> showMainMenu()
                is AppState.AccountSelection -> showAccountSelection()
                is AppState.AccountCreate -> showCreateAccount()
                is AppState.AccountMenu -> showAccountMenu(currentState.account)
                is AppState.Exit -> AppState.Exit
            }
        }

        println("Programm beendet.")

//        val account = service.createAccount("Max Mustermann")
//        println("Konto erstellt: ${account.id}")
//
//        val payment = Payment(
//            amount = BigDecimal("100.00"),
//            description = "Gehalt"
//        )
//
//        service.addPayment(account.id, payment)
//        println("Saldo: ${service.getBalance(account.id)} €")
    }

    fun showMainMenu(): AppState {
        println("==== Hauptmenü ====")
        println("1 - Account erstellen")
        println("2 - Account auswählen")
        println("3 - Beenden")
        print("Auswahl: ")

        return when (readln()) {
            "1" -> AppState.AccountCreate
            "2" -> AppState.AccountSelection
            "3" -> AppState.Exit
            else -> {
                println("Ungültige Eingabe")
                AppState.MainMenu
            }
        }
    }

    fun showAccountSelection(): AppState {
        println("==== Account auswählen ====")

        val accountsMap: MutableMap<UUID, Account> = service.getAllAccounts()

        accountsMap.forEach { (id, account) ->
            println("$id) ${account.owner}")
        }
        println("0 - Zurück")
        print("Auswahl: ")

        val input = readln().trim()

        // Zurück
        if (input == "0") {
            return AppState.MainMenu
        }

        // UUID parsen
        val uuid = try {
            UUID.fromString(input)
        } catch (e: IllegalArgumentException) {
            println("Ungültige UUID")
            return AppState.AccountSelection
        }

        // Account finden
        val account = accountsMap[uuid]
            ?: run {
                println("Account nicht gefunden")
                return AppState.AccountSelection
            }

        return AppState.AccountMenu(account)
    }

    fun showCreateAccount(): AppState {
        println("==== Account erstellen ====")
        println("Name eingeben: ")

        return AppState.MainMenu

        // TODO
//        return when (readLine()) {
//            //AppState.AccountMenu(account)
//
//            else -> {
//                println("Ungültige Eingabe")
//                AppState.MainMenu
//            }
//        }
    }

    fun showAccountMenu(account: Account): AppState {
        println("==== Account: ${account.owner} ====")
        println("1) Zahlungen anzeigen")
        println("2) Zahlung hinzufügen")
        println("0) Zurück")
        print("Auswahl: ")

        return when (readln()) {
            "1" -> {
                println("Zahlungen anzeigen für ${account.owner}")
                AppState.AccountMenu(account)
            }
            "2" -> {
                println("Zahlung hinzufügen für ${account.owner}")
                AppState.AccountMenu(account)
            }
            "0" -> AppState.MainMenu
            else -> {
                println("Ungültige Eingabe")
                AppState.AccountMenu(account)
            }
        }
    }
}

sealed class AppState {
    object MainMenu : AppState() // Hauptmenu
    object AccountCreate : AppState() // Account erstellen
    object AccountSelection : AppState() // Account auswählen
    data class AccountMenu(val account: Account) : AppState() // Menu wenn Account gewählt ist
    object Exit : AppState()
}