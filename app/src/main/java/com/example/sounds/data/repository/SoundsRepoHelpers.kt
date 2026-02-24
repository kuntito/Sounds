package com.example.sounds.data.repository


/**
 * Extracts the file extension from a URL pointing to a file, stripping any query parameters.
 *
 * Example:
 * ```
 * extractExtension("https://example.com/track.mp3?token=abc", "mp3")
 * // returns "mp3"
 * ```
 *                                                                                                                                                                  * @param url The URL to extract the extension from
 * @param default The fallback extension if none is found
 * @return The file extension without the leading dot
 */
fun extractExtension(url: String, default: String): String {
    return url
        .substringAfterLast('.')
        .substringBefore('?', default)
}