import java.util.UUID

class PaymentNotFoundException(id: UUID) :
    RuntimeException("Payment with ID $id not found")