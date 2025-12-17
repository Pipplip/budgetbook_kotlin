import java.math.BigDecimal
import java.util.UUID

class Account(
    val id: UUID = UUID.randomUUID(),
    val owner: String
) {
    private val payments = mutableListOf<Payment>()

    fun addPayment(payment: Payment) {
        payments.add(payment)
    }

    fun removePayment(paymentId: UUID) {
        payments.removeIf { it.id == paymentId }
    }

    fun balance(): BigDecimal =
        payments.fold(BigDecimal.ZERO) { acc, p -> acc + p.amount }

    fun payments(): List<Payment> =
        payments.toList()
}