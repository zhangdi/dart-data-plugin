package andrasferenczi._test

import java.io.File

fun File.findFirstParentFile(predicate: (File) -> Boolean): File? {
    var current: File? = this

    while (current !== null && !predicate(current)) {
        current = current.parentFile
    }

    return current
}
