import java.util.UUID

class AccountNotFoundException(id: UUID) :
    RuntimeException("Account with ID $id not found")