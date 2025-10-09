package ru.tbank.education.school.practice.files


import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Интерфейс для логирования ошибок в файл.
 */
interface ErrorLogger {

    /**
     * Логирует сообщение об ошибке в файл.
     *
     * @param message сообщение об ошибке
     * @param throwable исключение (если есть)
     * @return true, если запись успешна, иначе false
     */
    fun logError(message: String, throwable: Throwable?): Boolean
}

class IOErrorLogger(
    private val path: String
): ErrorLogger {
    override fun logError(message: String, throwable: Throwable?): Boolean {
        try {
            val writer = FileWriter(path)
            writer.use {
                val messsage = "${message} ${throwable?.message}\n"
                writer.append(messsage)
            }
            return true
        } catch(ex: IOException) { return false }
    }
}

class NIOErrorLogger(
    private val path: String
): ErrorLogger {
    override fun logError(message: String, throwable: Throwable?): Boolean {
        val messsage = "${message} ${throwable?.message}\n"
        try {
            Files.writeString(Paths.get(path), messsage, StandardOpenOption.APPEND)
            return true
        } catch(ex: IOException) { return false }

    }
}

fun main() {
    val path = "./lesson5/src/main/kotlin/ru/tbank/education/school/trycatchpractise/file_to_ErrorLogger.txt"
    val ioErrorLoger = IOErrorLogger(path)
    val nioErrorLoger = NIOErrorLogger(path)
    try {
        10 / 0
    } catch (ex: Exception) {
        ioErrorLoger.logError("Получена IO ошибка:", ex)
        nioErrorLoger.logError("Получена NIO ошибка:", ex)
    }

    try {
        "STRING".toInt()
    } catch (ex: Exception) {
        ioErrorLoger.logError("Получена IO ошибка:", ex)
        nioErrorLoger.logError("Получена NIO ошибка:", ex)
    }
}