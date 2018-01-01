package com.github.kory33.chatgui.util.collection

interface BijectiveMap<K, V> : Map<K, V> {
    /**
     * An inverse of the map.
     */
    val inverse: BijectiveMap<V, K>

    /**
     * Returns a new read-only map containing all key-value pairs from the original map.
     *
     * The returned map preserves the entry iteration order of the original map.
     */
    fun toMap(): Map<K, V>

    override val size: Int
        get() = toMap().size

    override val entries: Set<Map.Entry<K, V>>
        get() = toMap().entries

    override val keys: Set<K>
        get() = toMap().keys

    override val values: Set<V>
        get() = inverse.toMap().keys

    override fun get(key: K) = toMap()[key]

    override fun containsKey(key: K) = toMap().containsKey(key)

    override fun containsValue(value: V) = inverse.containsKey(value)

    override fun isEmpty() = toMap().isEmpty()
}