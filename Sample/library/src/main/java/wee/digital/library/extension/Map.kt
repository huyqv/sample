package wee.digital.library.extension

class MapValueNullException(key: String) :
    NullPointerException("value of key $key is null or empty")

fun Map<String, Any>?.strOrNull(key: String): String? {
    return this?.get(key) as? String
}

fun Map<String, Any>?.str(key: String): String {
    return strOrNull(key) ?: ""
}

fun Map<String, Any>?.strOrThrow(key: String): String {
    return strOrNull(key) ?: throw MapValueNullException(key)
}

fun Map<String, Any>?.intOrNull(key: String): Int? {
    return this?.get(key) as? Int
}

fun Map<String, Any>?.int(key: String): Int {
    return intOrNull(key) ?: 0
}

fun Map<String, Any>?.intOrThrow(key: String): Int {
    return intOrNull(key) ?: throw MapValueNullException(key)
}

fun Map<String, Any>?.longOrNull(key: String): Long? {
    return this?.get(key) as? Long
}

fun Map<String, Any>?.long(key: String): Long {
    return longOrNull(key) ?: 0
}

fun Map<String, Any>?.longOrThrow(key: String): Long {
    return longOrNull(key) ?: throw MapValueNullException(key)
}

fun Map<String, Any>?.doubleOrNull(key: String): Double? {
    return this?.get(key) as? Double
}

fun Map<String, Any>?.double(key: String): Double {
    return doubleOrNull(key) ?: 0.0
}

fun Map<String, Any>?.doubleOrThrow(key: String): Double {
    return doubleOrNull(key) ?: throw MapValueNullException(key)
}

fun Map<String, Any>?.mapOrNull(key: String): Map<String, Any>? {
    return this?.get(key) as? Map<String, Any>
}

fun Map<String, Any>?.map(key: String): Map<String, Any> {
    return mapOrNull(key) ?: mapOf()
}

fun Map<String, Any>?.mapOrThrow(key: String): Map<String, Any> {
    return mapOrNull(key) ?: throw MapValueNullException(key)
}

inline fun <reified T> Map<String, Any>?.arrayOrNull(key: String): Array<T>? {
    return this?.get(key) as? Array<T>
}

inline fun <reified T> Map<String, Any>?.array(key: String): Array<T> {
    return arrayOrNull(key) ?: arrayOf()
}

inline fun <reified T> Map<String, Any>?.arrayOrThrow(key: String): Array<T> {
    return arrayOrNull(key) ?: throw MapValueNullException(key)
}

inline fun <reified T> Map<String, Any>?.listOrNull(key: String): List<T>? {
    return (this?.get(key) as? Array<T>)?.toList()
}

inline fun <reified T> Map<String, Any>?.list(key: String): List<T> {
    return listOrNull(key) ?: listOf()
}

inline fun <reified T> Map<String, Any>?.listOrThrow(key: String): List<T> {
    return listOrNull(key) ?: throw MapValueNullException(key)
}

inline fun <reified T> Map<String, Any>?.list(key: String, transformer: (Map<String, Any>) -> T): List<T> {
    val list = mutableListOf<T>()
    (this?.get(key) as? Array<Map<String, Any>>)?.forEach {
        list.add(transformer(it))
    }
    return list
}