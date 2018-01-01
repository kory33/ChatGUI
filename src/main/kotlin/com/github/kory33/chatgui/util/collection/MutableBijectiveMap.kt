package com.github.kory33.chatgui.util.collection

interface MutableBijectiveMap<K, V> : BijectiveMap<K, V> {
    /**
     * Put a mapping from the given key to the given value.
     *
     * This method throws an [IllegalArgumentException]
     * if the mapping from the given key or to the given value already exists.
     */
    fun put(key: K, value: V)

    /**
     * Removes the specified key and its corresponding value from this map.
     * @param key key to be removed
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    fun removeKey(key: K): V?

    /**
     * Removes the specified value and its corresponding key from this map.
     * @param value value to be removed
     *
     * @return the previous key associated with the value, or `null` if the value was not present in the map.
     */
    fun removeValue(value: V): K?

    /**
     * Removes all elements from this map.
     */
    fun clear()
}