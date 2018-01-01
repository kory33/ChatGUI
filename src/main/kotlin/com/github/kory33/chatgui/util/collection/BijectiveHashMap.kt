package com.github.kory33.chatgui.util.collection

class BijectiveHashMap<K, V> : MutableBijectiveMap<K, V> {
    private val map: MutableMap<K, V>
    private val inverseMap: MutableMap<V, K>

    override val inverse: BijectiveMap<V, K>
        get() = BijectiveHashMap(this.inverseMap, this.map)

    constructor() {
        this.map = HashMap()
        this.inverseMap = HashMap()
    }

    constructor(bMap: BijectiveHashMap<K, V>) : this(bMap.map, bMap.inverseMap)

    private constructor(map: Map<K, V>, inverse: Map<V, K>) {
        this.map = map.toMutableMap()
        this.inverseMap = inverse.toMutableMap()
    }

    override fun put(key: K, value: V) {
        if (this.containsKey(key)) {
            throw IllegalArgumentException("There already exists a mapping from the given key.")
        }

        if (this.containsValue(value)) {
            throw IllegalArgumentException("There already exists a mapping to the given value.")
        }

        map.put(key, value)
        inverseMap.put(value, key)
    }

    override fun removeKey(key: K) = this.map.remove(key)?.also { this.inverseMap.remove(it) }

    override fun removeValue(value: V) = this.inverseMap.remove(value)?.also { this.map.remove(it) }

    override fun clear() = this.map.clear().also { this.inverseMap.clear() }

    override operator fun get(key: K) = this.map[key]

    override fun containsKey(key: K) = this.map.containsKey(key)

    override fun containsValue(value: V) = this.inverseMap.containsKey(value)

    override fun toMap() = map.toMap()
}
