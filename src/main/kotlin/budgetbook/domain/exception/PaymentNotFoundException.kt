
import java.util.UUID

class PaymentNotFoundException(id: UUID) :
    RuntimeException("Konto mit ID $id wurde nicht gefunden")