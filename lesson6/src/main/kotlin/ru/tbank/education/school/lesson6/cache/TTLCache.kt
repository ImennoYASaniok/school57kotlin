package ru.tbank.education.school.lesson6.cache

/**
 * Интерфейс для простого кэша с временем жизни записей.
 * Автоматически удаляет устаревшие записи при обращении.
 */
interface TTLCache<K, V> {
    
    /**
     * Помещает значение в кэш с указанным временем жизни.
     * @param key Ключ для доступа к значению
     * @param value Сохраняемое значение
     * @param ttlMs Время жизни записи в миллисекундах
     */
    fun put(key: K, value: V, ttlMs: Long)
    
    /**
     * Помещает значение в кэш со временем жизни по умолчанию (60 секунд).
     * @param key Ключ для доступа к значению
     * @param value Сохраняемое значение
     */
    fun put(key: K, value: V)
    
    /**
     * Получает значение из кэша по ключу.
     * Автоматически удаляет устаревшие записи при обращении.
     * @param key Ключ для поиска значения
     * @return Значение или null если не найдено или устарело
     */
    fun get(key: K): V?
}

data class TTLCacheValue<V>(val value: V, val ttlTime: Long)

class TTLCacheImpl<K, V>: TTLCache<K, V> {
    protected val hashData = HashMap<K, TTLCacheValue<V>>()

    override fun put(key: K, value: V, ttlMs: Long) {
        hashData[key] = TTLCacheValue(value, ttlMs + System.currentTimeMillis())
    }

    override fun put(key: K, value: V) {
        hashData[key] = TTLCacheValue(value, 60000 + System.currentTimeMillis())
    }

    override fun get(key: K): V? {
        if (key in hashData) {
            if(System.currentTimeMillis() < hashData[key]!!.ttlTime) {
                return hashData[key]!!.value
            } else { hashData.remove(key); return null }
        } else return null
    }
}

// System.currentTimeMillis()