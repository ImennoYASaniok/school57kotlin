package ru.tbank.education.school.lesson6.cache

/**
 * Интерфейс для кэша с политикой LRU (Least Recently Used).
 * Кэш хранит ограниченное количество элементов.
 * Когда кэш переполняется, удаляется самый "старый" элемент.
 */
interface LRUCache<K, V> {
    /**
     * Добавляет значение value под ключом key в кэш.
     * Если ключ уже есть, обновляет его и помечает как недавно использованный.
     */
    fun put(key: K, value: V)

    /**
     * Получает значение по ключу key и помечает его как недавно использованное.
     * Возвращает null, если ключа нет в кэше.
     */
    fun get(key: K): V?

    /**
     * Удаляет значение по ключу key.
     */
    fun remove(key: K)

    /**
     * Очищает весь кэш.
     */
    fun clear()
    
    /**
     * Возвращает текущий размер кэша.
     */
    fun size(): Int
}


class LRUCacheImpl<K, V>(val CashSize: Int): LRUCache<K, V> {
    protected val cacheData = LinkedHashMap<K, V>()

    override fun put(key: K, value: V) {
        if (cacheData.containsKey(key)) cacheData.remove(key)
        cacheData[key] = value
        if (cacheData.size > CashSize) cacheData.remove(cacheData.keys.first())
    }

    override fun get(key: K): V? {
        if (cacheData.containsKey(key)) {
            val copyCacheItem: V = cacheData[key]!!
            cacheData.remove(key)
            cacheData[key] = copyCacheItem
            return cacheData[key]
        } else return null
    }

    override fun remove(key: K) {
        cacheData.remove(key)
    }

    override fun clear() {
        cacheData.clear()
    }

    override fun size(): Int {
        return cacheData.size
    }

}
