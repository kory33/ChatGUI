package com.github.kory33.chatgui.util.collection

class BijectiveHashMap<K, V> : HashMap<K, V> {
    private val inverse: HashMap<V, K>

    constructor() {
        this.inverse = HashMap<V, K>()
    }

    private constructor(map: HashMap<K, V>, inverse: HashMap<V, K>) : super(map) {
        this.inverse = HashMap(inverse)
    }

    constructor(bMap: BijectiveHashMap<K, V>) : this(bMap, bMap.inverse)

    override fun put(key: K, value: V): V? {
        if (this.containsKey(key)) {
            this.inverse.remove(this[key])
        }

        if (this.inverse.containsKey(value)) {
            throw IllegalArgumentException("There already exists a mapping to the given value.")
        }

        this.inverse.put(value, key)
        return super.put(key, value)
    }

    /**
     * Remove the mapping pair which has the given key.
     * @param key key to be removed
     * *
     * @return value which was mapped from removed key. Null if the key was not in the key set.
     */
    override fun remove(key: K): V? {
        if (!this.containsKey(key)) {
            return null
        }

        val removed = super.remove(key)
        this.inverse.remove(removed)
        return removed
    }

    /**
     * Remove the mapping pair which has the given value.
     * @param value value to be removed from the map
     * *
     * @return key which was mapped to removed value. Null if the value was not in the value set.
     */
    fun removeValue(value: V): K? {
        val removed = this.inverse.remove(value)

        if (removed != null) {
            this.remove(removed)
        }

        return removed
    }

    /**
     * Get an inverse of the map.
     * @return inverse map
     */
    fun getInverse(): BijectiveHashMap<V, K> {
        return BijectiveHashMap(this.inverse, this)
    }
}
