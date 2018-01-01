package com.github.kory33.chatgui.util.collection

/**
 * Put a mapping corresponding to the given pair.
 *
 * This method throws an [IllegalArgumentException]
 * if the mapping from the given key or to the given value already exists.
 */
fun <K, V> MutableBijectiveMap<K, V>.put(pair: Pair<K, V>) = put(pair.first, pair.second)

/**
 * Updates a key bind to a value.
 *
 * When there already exists a mapping of
 * oldKey -> [value], this method replaces the mapping to
 * [key] -> [value].
 */
fun <K, V> MutableBijectiveMap<K, V>.updateKey(key: K, value: V) = removeValue(value).run { put(key, value) }

/**
 * Updates a value bind to a key.
 *
 * When there already exists a mapping of
 * [key] -> oldValue, this method replaces the mapping to
 * [key] -> [value].
 */
fun <K, V> MutableBijectiveMap<K, V>.updateValue(key: K, value: V) = removeKey(key).run { put(key, value) }

/**
 * Updates a mapping from the key or to the value.
 *
 * When there already exist mappings of
 * [key] -> oldValue or oldValue -> [key], this method replaces them to
 * [key] -> [value].
 */
fun <K, V> MutableBijectiveMap<K, V>.updatePair(key: K, value: V) {
    updateKey(key, value)
    updateValue(key, value)
}