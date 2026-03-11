package com.retoday.core.global.extension

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

private const val RANDOM_BITS = 22
private const val RANDOM_MASK = (1 shl RANDOM_BITS) - 1

private val tsidSequence = AtomicInteger(0)

@Volatile
private var tsidLastTimestamp = 0L

fun createTsid(): Long {
    synchronized(tsidSequence) {
        val now = System.currentTimeMillis()
        if (now != tsidLastTimestamp) {
            tsidLastTimestamp = now
            tsidSequence.set(ThreadLocalRandom.current().nextInt(RANDOM_MASK + 1))
        }

        return (now shl RANDOM_BITS) or (tsidSequence.getAndIncrement().toLong() and RANDOM_MASK.toLong())
    }
}
