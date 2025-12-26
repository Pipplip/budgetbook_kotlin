interface PersistableRepository {
    fun persistAllToFile(): Boolean
    fun loadFromFile()
}