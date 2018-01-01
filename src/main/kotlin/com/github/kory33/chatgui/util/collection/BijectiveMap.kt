package com.github.kory33.chatgui.util.collection

interface BijectiveMap<K, V> {
    val keys: Set<K>
        get() = toMap().keys

    val values: Set<V>
        get() = inverse.keys

    val inverse: BijectiveMap<V, K>

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the map.
     */
    operator fun get(key: K): V?

    /**
     * Returns a boolean value representing if the map contains the given key.
     *
     * @return true if the map contains the given key
     */
    fun containsKey(key: K): Boolean

    /**
     * Returns a boolean value representing if the map contains the given value.
     *
     * @return true if the map contains the given value
     */
    fun containsValue(value: V): Boolean

    /**
     * Returns a new read-only map containing all key-value pairs from the original map.
     *
     * The returned map preserves the entry iteration order of the original map.
     */
    fun toMap(): Map<K, V>
}