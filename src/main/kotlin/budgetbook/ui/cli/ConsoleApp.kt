import java.time.Month
import java.util.*

class ConsoleApp(
    private val service: AccountService
) {

    var state: AppState = AppState.MainMenu

    fun run() {
        println("==== Willkommen zum BudgetBook CLI Tool ====")

        while (state != AppState.Exit) {

            val currentState = state

            state = when (currentState) {
                AppState.MainMenu -> showMainMenu()
                AppState.AccountSelection -> showAccountSelection()
                AppState.AccountCreate -> showCreateAccount()
                AppState.AccountDelete -> showDeleteAccount()
                AppState.AccountSave -> showSaveAll()
                is AppState.AccountMenu -> showAccountMenu(currentState.account)
                is AppState.ListPayments -> showAccountMenuListPayments(currentState.account)
                is AppState.AddPayment -> showAccountMenuAddPayment(currentState.account)
                is AppState.DeletePayment -> showAccountMenuDeletePayment(currentState.account)
                is AppState.ShowBalance -> showAccountMenuShowBalanceForMonth(currentState.account)
                AppState.Exit -> AppState.Exit
            }
        }

        println("Programm beendet.")
    }

    // -------- Main Menu --------

    fun showMainMenu(): AppState {
        println()
        println("==== Hauptmenü ====")
        println("1 - Account erstellen")
        println("2 - Account auswählen")
        println("3 - Account löschen")
        println("4 - Speichern")
        println("5 - Beenden")
        print("Auswahl: ")

        return when (readln().trim()) {
            "1" -> AppState.AccountCreate
            "2" -> AppState.AccountSelection
            "3" -> AppState.AccountDelete
            "4" -> AppState.AccountSave
            "5" -> AppState.Exit
            else -> {
                invalid(AppState.MainMenu)
            }
        }
    }

    // -------- Account --------

    fun showAccountSelection(): AppState {
        println()
        println("==== Account auswählen ====")

        val accounts = service.getAllAccounts()
        if(accounts.isEmpty()) {
            return invalid(AppState.MainMenu)
        }

        accounts.forEachIndexed { index, account ->
            println("${index + 1} - ${account.owner} id: ${account.id}")
        }

        println("0 - Zurück")
        print("Auswahl: ")

        val input = readln().trim()

        // Zurück
        if (input == "0") return AppState.MainMenu

        val index = input.toIntOrNull()
        if(index == null || index !in 1..accounts.size){
            return invalid(AppState.AccountSelection)
        }

        val account = accounts[index - 1]
        return AppState.AccountMenu(account)
    }

    fun showCreateAccount(): AppState {
        println()
        println("==== Account erstellen ====")
        println("Name eingeben: ")

        val input = readln().trim()

        if(input.isEmpty()){
            return invalid(AppState.MainMenu)
        }

        val account = service.createAccount(input)
        println("Konto erstellt: ${account.owner} - ${account.id}")
        return AppState.AccountMenu(account)
    }

    fun showDeleteAccount(): AppState {
        println()
        println("==== Account löschen ====")
        println("ID: ")

        val input = readln().trim()
        val uuid = try {
            UUID.fromString(input)
        }catch (e: Exception){
            println("Ungültige UUID")
            return AppState.MainMenu
        }

        if(service.deleteAccount(uuid)){
            println("Konto gelöscht")
        }else{
            println("Konto konnte nicht gelöscht werden")
        }

        return AppState.MainMenu
    }

    fun showSaveAll(): AppState {
        println()
        println("==== Speichern? ====")
        println("y/n")

        return when (readln().trim()) {
            "y" -> {
                if(service.saveAll()){
                    println("Speichern erfolgreich!")
                }else{
                    println("Speichern nicht erfolgreich!")
                }
                AppState.MainMenu
            }
            "n" -> AppState.MainMenu
            else -> {
                println("Ungültige Eingabe")
                AppState.MainMenu
            }
        }
    }

    // -------- Account Menu --------

    fun showAccountMenu(account: Account): AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("1) Zahlungen anzeigen")
        println("2) Zahlung hinzufügen")
        println("3) Zahlung löschen")
        println("4) Balance für Monat anzeigen")
        println("0) Zurück")
        print("Auswahl: ")

        return when (readln().trim()) {
            "1" -> {
                println("Zahlungen anzeigen für ${account.owner}")
                AppState.ListPayments(account)
            }
            "2" -> {
                println("Zahlung hinzufügen für ${account.owner}")
                AppState.AddPayment(account)
            }
            "3" -> {
                println("Zahlung löschen für ${account.owner}")
                AppState.DeletePayment(account)
            }
            "4" -> {
                AppState.ShowBalance(account)
            }
            "0" -> AppState.MainMenu
            else -> {
                invalid(AppState.AccountMenu(account))
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

        for (payment in account.getPayments()) {
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

    // -------- Payments --------

    fun showAccountMenuAddPayment(account: Account) : AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("Zahlung eingeben (Betrag + Beschreibung)")
        print("Eingabe: ")

        val input = readln().trim()

        val parts = input.split(" ", limit = 2)
        if (parts.size != 2) {
            return invalid(AppState.AccountMenu(account))
        }

        val amount = parts[0].toBigDecimalOrNull()
        val desc = parts[1]

        if (amount == null) {
            println("Ungültiger Betrag")
            return AppState.AccountMenu(account)
        }

        val payment = service.addPayment(account.id, amount, desc)
        println("Zahlung hinzugefügt: ${payment.amount} - ${payment.description} - ${payment.id}")

        return AppState.AccountMenu(account)
    }

    fun showAccountMenuDeletePayment(account: Account) : AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("Zahlung löschen (id)")
        print("Eingabe: ")

        val input = readln().trim()
        val uuid = try {
            UUID.fromString(input)
        } catch (e: IllegalArgumentException) {
            println("Ungültige UUID")
            return AppState.AccountMenu(account)
        }

        service.removePayment(account.id, uuid)
        println("Zahlung gelöscht: $uuid")

        return AppState.AccountMenu(account)
    }

    fun showAccountMenuShowBalanceForMonth(account: Account) : AppState {
        println()
        println("==== Account: ${account.owner} ====")
        println("Balance für einen bestimmten Monat anzeigen (Monat Jahr z.B. 2 2025)")
        print("Eingabe: ")

        val input = readln().trim()
        if(input.isEmpty()){
            return invalid(AppState.AccountMenu(account))
        }

        val parts = input.split(" ", limit = 2)
        if (parts.size != 2) {
            return invalid(AppState.AccountMenu(account))
        }

        val month = parts[0].toIntOrNull()
        val year = parts[1].toIntOrNull()
        if (month == null || year == null || month !in 1..12) {
            println("Ungültiger Monat oder Jahr")
            return AppState.AccountMenu(account)
        }

        val balance = service.getBalanceFor(account.id, year, Month.of(month))

        println("Balance für $month/$year: $balance")

        return AppState.AccountMenu(account)

    }

    // -------- Helpers --------
    private fun invalid(next: AppState): AppState {
        println("Ungültige Eingabe.")
        return next
    }
}

sealed class AppState {
    // ---- Main Menu ----
    object MainMenu : AppState()
    object AccountSelection : AppState()
    object AccountCreate : AppState()
    object AccountDelete : AppState()
    object AccountSave : AppState()
    object Exit : AppState()

    // ---- Account Menu ----
    data class AccountMenu(val account: Account) : AppState()
    data class ListPayments(val account: Account) : AppState()
    data class AddPayment(val account: Account) : AppState()
    data class DeletePayment(val account: Account) : AppState()
    data class ShowBalance(val account: Account) : AppState()
}