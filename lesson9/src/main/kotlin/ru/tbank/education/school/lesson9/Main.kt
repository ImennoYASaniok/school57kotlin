package ru.tbank.education.school.lesson9

import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class SimpleZipper(private val allowedExtensions: Set<String> = setOf("txt", "logs")) {

    @Throws(IOException::class)
    fun zipDirectory(sourceDir: File, zipFile: File) {
        if (!sourceDir.exists() || !sourceDir.isDirectory) {
            throw IllegalArgumentException("Некорректно указан путь до конвертирующеся директории")
        }

        val parent = zipFile.parentFile
        if (parent != null && !parent.exists()) parent.mkdirs()

        FileOutputStream(zipFile).use { fos ->
            BufferedOutputStream(fos).use { bos ->
                ZipOutputStream(bos).use { zos ->
                    addFiles(
                        file = sourceDir,
                        baseCanonical = sourceDir.canonicalPath,
                        zos = zos,
                        zipCanonical = zipFile.canonicalPath
                    )
                }
            }
        }

        println("Архив создан по адресу: ${zipFile.absolutePath}")
    }

    @Throws(IOException::class)
    private fun addFiles(
        file: File,
        baseCanonical: String,
        zos: ZipOutputStream,
        zipCanonical: String
    ) {
        if (file.canonicalPath == zipCanonical) return

        if (file.isDirectory) {
            file.listFiles()?.forEach {
                addFiles(it, baseCanonical, zos, zipCanonical)
            }
        } else {
            val name = file.name
            val idx = name.lastIndexOf('.')
            val ext = if (idx >= 0) name.substring(idx + 1).lowercase(Locale.getDefault()) else ""
            if (!allowedExtensions.contains(ext)) return

            val entryName = makeRelative(file, baseCanonical)

            val entry = ZipEntry(entryName)
            entry.time = file.lastModified()
            zos.putNextEntry(entry)

            FileInputStream(file).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    val buffer = ByteArray(4096)
                    var len: Int
                    while (bis.read(buffer).also { len = it } != -1) {
                        zos.write(buffer, 0, len)
                    }
                }
            }

            zos.closeEntry()
            println("Добавлен в архив: $entryName")
        }
    }

    @Throws(IOException::class)
    private fun makeRelative(file: File, baseCanonical: String): String {
        var rel = file.canonicalPath.substring(baseCanonical.length)
        while (rel.startsWith(File.separator)) rel = rel.substring(1)
        return rel.replace(File.separatorChar, '/')
    }
}


fun main() {
    // Я написал класс вместо функции, так как у меня получилось довольно много методов отдельных для работы архиватора, поэтому чтобы удобно обращатся я использовал класс

    val zipper = SimpleZipper() // По умолчанию архивирует txt и logs

    zipper.zipDirectory(
        sourceDir = File("lesson9/logs"),
        zipFile = File("lesson9/logs_archive.zip")
    )
}
