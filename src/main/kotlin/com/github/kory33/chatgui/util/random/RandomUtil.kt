package com.github.kory33.chatgui.util.random

import java.util.*

tailrec fun Random.nextLongNotIn(set: Set<Long>): Long {
    val randomLong = nextLong()
    return if (!set.contains(randomLong)) randomLong else nextLongNotIn(set)
}
