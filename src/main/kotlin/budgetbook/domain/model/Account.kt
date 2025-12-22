import java.math.BigDecimal
import java.time.Month
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
class Account(
    @Serializable(with = UUIDSerializer::class)
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

    fun getBalanceForSpecificMonthYear(year: Int, month: Month) : BigDecimal =
        payments
            .filter { it.date.year == year && it.date.month == month }
            .sumOf { it.amount }

    fun payments(): List<Payment> =
        payments.toList()
}