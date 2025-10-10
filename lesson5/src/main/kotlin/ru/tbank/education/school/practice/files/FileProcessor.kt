package ru.tbank.education.school.practice.files

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
/**
 * Интерфейс для безопасной работы с текстовыми файлами.
 */
interface FileProcessor {

    /**
     * Читает все строки из файла и возвращает их список.
     *
     * Если файл не существует — метод должен поймать `FileNotFoundException` и вернуть пустой список.
     * Если возникает ошибка при чтении — метод должен поймать `IOException` и вернуть null.
     *
     * @param path путь к файлу
     * @return список строк файла, пустой список, если файла нет, или null при другой ошибке
     */
    fun readFile(path: String): List<String>?
}

class IOFileProcessor: FileProcessor {
    override fun readFile(path: String): List<String>? {
        val lines: MutableList<String> = mutableListOf()
        var line: String? = null

        try {
            val file = File(path)
            file.bufferedReader().use { reader -> while(reader.readLine().also { line = it } != null) lines.add(line!!) }
            return lines.toList()
        } catch (ex: FileNotFoundException) { return emptyList() }
        catch (ex: IOException) { throw ex }

    }
}

class NIOFileProcessor: FileProcessor {
    override fun readFile(path: String): List<String>? {
        val path = Paths.get(path)
        try { return Files.readAllLines(path) }
        catch (ex: NoSuchFileException) { return emptyList() }
        catch (ex: IOException) { throw ex }
    }
}

fun main() {

    val ioFileProccessor = IOFileProcessor()
    val nioFileProcessor = NIOFileProcessor()

    val path = "./lesson5/src/main/kotlin/ru/tbank/education/school/trycatchpractise/file_toFileProcessor.txt"
    val ioLines = ioFileProccessor.readFile(path)
    println("Содержимое ioLines:")
    for (ind in 1..ioLines!!.size - 1) println(ioLines[ind])
    println()
    val nioLines = nioFileProcessor.readFile(path)
    println("Содержимое nioLines:")
    for (ind in 1..nioLines!!.size - 1) println(nioLines[ind])
}