import java.time.LocalDate
import java.time.Month
import java.util.*

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
                is AppState.AccountDelete -> showDeleteAccount()
                is AppState.AccountMenu -> showAccountMenu(currentState.account)
                is AppState.AccountMenuListPayments -> showAccountMenuListPayments(currentState.account)
                is AppState.AccountMenuAddPayment -> showAccountMenuAddPayment(currentState.account)
                is AppState.AccountMenuDeletePayment -> showAccountMenuDeletePayment(currentState.account)
                is AppState.AccountMenuShowBalanceForMonth -> showAccountMenuShowBalanceForMonth(currentState.account)
                is AppState.Exit -> AppState.Exit
            }
        }

        println("Programm beendet.")
    }

    fun showMainMenu(): AppState {
        println()
        println("==== Hauptmenü ====")
        println("1 - Account erstellen")
        println("2 - Account auswählen")
        println("3 - Account löschen")
        println("4 - Beenden")
        print("Auswahl: ")

        return when (readln().trim()) {
            "1" -> AppState.AccountCreate
            "2" -> AppState.AccountSelection
            "3" -> AppState.AccountDelete
            "4" -> AppState.Exit
            else -> {
                println("Ungültige Eingabe")
                AppState.MainMenu
            }
        }
    }

    fun showAccountSelection(): AppState {
        println()
        println("==== Account auswählen ====")

        val accountsMap: MutableMap<UUID, Account> = service.getAndInitAllAccounts()

        val accountToList = accountsMap.values.toList()

        accountToList.forEachIndexed { index, account ->
            println("${index + 1} - ${account.owner} id: ${account.id}")
        }

        println("0 - Zurück")
        print("Auswahl: ")

        val input = readln().trim()

        // Zurück
        if (input == "0") return AppState.MainMenu

        val index = input.toIntOrNull()
        if(index == null || index !in 1..accountToList.size){
            println("Ungültige Eingabe.")
            return AppState.MainMenu
        }

        // UUID parsen und Account finden
        val account = runCatching {
            val selectedAccount = accountToList[index - 1]
            UUID.fromString(selectedAccount.id.toString())
        }.getOrElse {
            println("Ungültige UUID")
            return AppState.AccountSelection
        }.let { uuid ->
            accountsMap[uuid] ?: run {
                println("Account nicht gefunden")
                return AppState.AccountSelection
            }
        }

        return AppState.AccountMenu(account)
    }

    fun showCreateAccount(): AppState {
        println()
        println("==== Account erstellen ====")
        println("Name eingeben: ")

        val input = readln().trim()

        if(input.isEmpty()){
            println("Ungültige Eingabe")
            return AppState.MainMenu
        }else{
            val account = service.createAccount(input)
            println("Konto erstellt: ${account.owner} - ${account.id}")
            return AppState.AccountMenu(account)
        }
    }

    fun showDeleteAccount(): AppState {
        println()
        println("==== Account löschen ====")
        println("ID: ")

        val input = readln().trim()

        if(input.isEmpty()){
            println("Ungültige Eingabe")
            return AppState.MainMenu
        }else{
            val account = service.deleteAccount(UUID.fromString(input))
            println("Konto gelöscht")
            return AppState.MainMenu
        }
    }

    fun showAccountMenu(account: Account): AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("1) Zahlungen anzeigen")
        println("2) Zahlung hinzufügen")
        println("3) Zahlung löschen")
        println("4) Balance für einen bestimmten Monat anzeigen")
        println("0) Zurück")
        print("Auswahl: ")

        return when (readln().trim()) {
            "1" -> {
                println("Zahlungen anzeigen für ${account.owner}")
                AppState.AccountMenuListPayments(account)
            }
            "2" -> {
                println("Zahlung hinzufügen für ${account.owner}")
                AppState.AccountMenuAddPayment(account)
            }
            "3" -> {
                println("Zahlung löschen für ${account.owner}")
                AppState.AccountMenuDeletePayment(account)
            }
            "4" -> {
                AppState.AccountMenuShowBalanceForMonth(account)
            }
            "0" -> AppState.MainMenu
            else -> {
                println("Ungültige Eingabe")
                AppState.AccountMenu(account)
            }
        }
    }

    fun showAccountMenuListPayments(account: Account) : AppState{
        if(!service.hasPayments(account.id)){
            println("Keine Zahlungen vorhanden!")
            return AppState.AccountMenu(account)
        }
//        for (payment in account.payments()){
//            println("${payment.amount} - ${payment.description} Datum: ${payment.date} - id: ${payment.id}")
//        }

        println(
            String.format(
                "%-10s | %-30s | %-12s | %-5s",
                "Betrag", "Beschreibung", "Datum", "ID"
            )
        )
        println("-".repeat(65))

        for (payment in account.payments()) {
            println(
                String.format(
                    "%-10s | %-30s | %-12s | %-5s",
                    payment.amount,
                    payment.description,
                    payment.date,
                    payment.id
                )
            )
        }
        return AppState.AccountMenu(account)
    }

    fun showAccountMenuAddPayment(account: Account) : AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("Zahlung eingeben (Betrag + Beschreibung)")
        print("Eingabe: ")

        val input = readln().trim()

        val parts = input.split(" ", limit = 2)
        if (parts.size != 2) {
            println("Ungültige Eingabe")
            return AppState.AccountMenu(account)
        }

        val amountString = parts[0]
        val desc = parts[1]

        val amount = amountString.toBigDecimalOrNull()
        if (amount == null) {
            println("Ungültiger Betrag")
            return AppState.AccountMenu(account)
        }

        val payment = service.addPayment(account.id, amount, desc)
        println("Zahlung hinzugefügt: $amount - $desc - ${payment.id}")
        return AppState.AccountMenu(account)
    }

    fun showAccountMenuDeletePayment(account: Account) : AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("Zahlung löschen (id)")
        print("Eingabe: ")

        val input = readln().trim()
        if(input.isEmpty()){
            println("Ungültige Eingabe")
            return AppState.AccountMenu(account)
        }else {
            val uuid = try {
                UUID.fromString(input)
            } catch (e: IllegalArgumentException) {
                println("Ungültige UUID: ${e.message}")
                return AppState.AccountMenu(account)
            }
            service.removePayment(account.id, uuid)
            println("Zahlung gelöscht: $uuid")
        }
        return AppState.AccountMenu(account)
    }

    fun showAccountMenuShowBalanceForMonth(account: Account) : AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("Balance für einen bestimmten Monat anzeigen (Monat Jahr z.B. 2 2025)")
        print("Eingabe: ")
        val input = readln().trim()
        if(input.isEmpty()){
            println("Ungültige Eingabe")
            return AppState.AccountMenu(account)
        }else{
            val parts = input.split(" ", limit = 2)
            if (parts.size != 2) {
                println("Ungültige Eingabe")
                return AppState.AccountMenu(account)
            }

            val month = parts[0]
            val year = parts[1]
            val balance = service.getBalanceForSpecificMonthYear(account.id, year.toInt(), Month.of(month.toInt()))

            println("Balance: $balance")

            return AppState.AccountMenu(account)
        }
    }
}

sealed class AppState {
    object MainMenu : AppState() // Hauptmenu
    object AccountCreate : AppState() // Account erstellen
    object AccountDelete : AppState() // Account erstellen
    object AccountSelection : AppState() // Account auswählen
    data class AccountMenu(val account: Account) : AppState() // Menu wenn Account gewählt ist
    object Exit : AppState()
    data class AccountMenuListPayments(val account: Account): AppState()
    data class AccountMenuAddPayment(val account: Account): AppState()
    data class AccountMenuDeletePayment(val account: Account): AppState()
    data class AccountMenuShowBalanceForMonth(val account: Account): AppState()
}