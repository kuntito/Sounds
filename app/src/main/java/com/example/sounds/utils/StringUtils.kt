package com.example.sounds.utils

/**
 * checks if [query] is a subsequence of [target].
 *
 * all characters in [query] must appear in [target] in the same order,
 * but not necessarily next to each other.
 * "soapi" matches "SoundsApi" but not "apiSounds".
 *
 * returns false if [query] is empty.
 */
fun subsequenceMatch(query: String, target: String, ignoreCase: Boolean = true): Boolean {
    if (query.isEmpty()) return false
    if (query.length > target.length) return false

    var queryIdx = 0
    target.forEach { ch ->
        if (ch.equals(query[queryIdx], ignoreCase = ignoreCase)) {
            queryIdx += 1
        }

        if (queryIdx == query.length) return true
    }

    return false
}