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
     * Put a mapping corresponding to the given pair.
     *
     * This method throws an [IllegalArgumentException]
     * if the mapping from the given key or to the given value already exists.
     */
    fun put(pair: Pair<K, V>) = put(pair.first, pair.second)

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

    /**
     * Updates a key bind to a value.
     *
     * When there already exists a mapping of
     * oldKey -> [value], this method replaces the mapping to
     * [key] -> [value].
     */
    fun updateKey(key: K, value: V) = removeValue(value).run { put(key, value) }

    /**
     * Updates a value bind to a key.
     *
     * When there already exists a mapping of
     * [key] -> oldValue, this method replaces the mapping to
     * [key] -> [value].
     */
    fun updateValue(key: K, value: V) = removeKey(key).run { put(key, value) }

    /**
     * Updates a mapping from the key or to the value.
     *
     * When there already exist mappings of
     * [key] -> oldValue or oldValue -> [key], this method replaces them to
     * [key] -> [value].
     */
    fun updatePair(key: K, value: V) = updateKey(key, value).run { updateValue(key, value) }
}