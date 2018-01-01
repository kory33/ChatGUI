package com.github.kory33.chatgui.util.collection

/**
 * The number of key/value pairs in the map.
 */
val <K, V> BijectiveMap<K, V>.size: Int
    get() = toMap().size

/**
 * Returns a read-only [Set] of all keys in this map.
 */
val <K, V> BijectiveMap<K, V>.keys: Set<K>
    get() = toMap().keys

/**
 * Returns a read-only [Set] of all values in this map.
 */
val <K, V> BijectiveMap<K, V>.values: Set<V>
    get() = inverse.keys
