import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Payment(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate = LocalDate.now(),
    val description: String
)