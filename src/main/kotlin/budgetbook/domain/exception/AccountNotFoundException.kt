
import java.util.UUID

class AccountNotFoundException(id: UUID) :
    RuntimeException("Konto mit ID $id wurde nicht gefunden")