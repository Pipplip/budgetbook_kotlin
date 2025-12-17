import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class Payment(
    val id: UUID = UUID.randomUUID(),
    val amount: BigDecimal,
    val date: LocalDate = LocalDate.now(),
    val description: String
)