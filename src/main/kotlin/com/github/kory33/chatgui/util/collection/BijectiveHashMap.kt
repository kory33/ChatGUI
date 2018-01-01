package com.github.kory33.chatgui.util.collection

class BijectiveHashMap<K, V> {
    private val map: MutableMap<K, V>
    private val inverse: MutableMap<V, K>

    val values
        get() = inverse.keys
    val keys
        get() = map.keys

    constructor() {
        this.map = HashMap()
        this.inverse = HashMap()
    }

    constructor(bMap: BijectiveHashMap<K, V>) : this(bMap.map, bMap.inverse)

    private constructor(map: Map<K, V>, inverse: Map<V, K>) {
        this.map = map.toMutableMap()
        this.inverse = inverse.toMutableMap()
    }

    /**
     * Put a mapping from the given key to the given value.
     *
     * This method throws an [IllegalArgumentException]
     * if the mapping from the given key or to the given value already exists.
     */
    fun put(key: K, value: V) {
        if (this.containsKey(key)) {
            throw IllegalArgumentException("There already exists a mapping from the given key.")
        }

        if (this.containsValue(value)) {
            throw IllegalArgumentException("There already exists a mapping to the given value.")
        }

        map.put(key, value)
        inverse.put(value, key)
    }

    /**
     * Put a mapping corresponding to the given pair.
     *
     * This method throws an [IllegalArgumentException]
     * if the mapping from the given key or to the given value already exists.
     */
    fun put(pair: Pair<K, V>) = put(pair.first, pair.second)

    /**
     * Updates a key bind to a value.
     *
     * When there already exists a mapping of
     * oldKey -> [value], this method replaces the mapping to
     * [key] -> [value].
     */
    fun updateKey(key: K, value: V) = removeValue(value).run { put(key, value) }

    /**
     * Updates a key bind to a value.
     *
     * When there already exists a mapping of
     * [key] -> oldValue, this method replaces the mapping to
     * [key] -> [value].
     */
    fun updateValue(key: K, value: V) = removeKey(key).run { put(key, value) }

    fun updatePair(key: K, value: V) = updateKey(key, value).run { updateValue(key, value) }

    /**
     * Remove the mapping pair which has the given key.
     * @param key key to be removed
     * *
     * @return value which was mapped from removed key. Null if the key was not in the key set.
     */
    fun removeKey(key: K) = this.map.remove(key)?.also { this.inverse.remove(it) }

    /**
     * Remove the mapping pair which has the given value.
     * @param value value to be removed from the map
     * *
     * @return key which was mapped to removed value. Null if the value was not in the value set.
     */
    fun removeValue(value: V) = this.inverse.remove(value)?.also { this.map.remove(it) }

    /**
     * Clear the entire map.
     */
    fun clear() = this.map.clear().also { this.inverse.clear() }

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the map.
     */
    operator fun get(key: K) = this.map[key]

    /**
     * Returns a boolean value representing if the map contains the given key.
     *
     * @return true if the map contains the given key
     */
    fun containsKey(key: K) = this.map.containsKey(key)

    /**
     * Returns a boolean value representing if the map contains the given value.
     *
     * @return true if the map contains the given value
     */
    fun containsValue(value: V) = this.inverse.containsKey(value)

    /**
     * Returns an inverse of the map.
     * @return inverse map
     */
    fun getInverse(): BijectiveHashMap<V, K> {
        return BijectiveHashMap(this.inverse, this.map)
    }
}
