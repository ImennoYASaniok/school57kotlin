package ru.tbank.education.school.homework

import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


/**
 * Интерфейс для подсчёта строк и слов в файле.
 */
interface FileAnalyzer {

    /**
     * Считает количество строк и слов в указанном входном файле и записывает результат в выходной файл.
     *
     * Словом считается последовательность символов, разделённая пробелами,
     * табуляциями или знаками перевода строки. Пустые части после разделения не считаются словами.
     *
     * @param inputFilePath путь к входному текстовому файлу
     * @param outputFilePath путь к выходному файлу, в который будет записан результат
     * @return true если операция успешна, иначе false
     */
    fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean
}

/**
 * Реализация через Java IO
 */
class IOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        try {
            val inputFile = File(inputFilePath)
            val lines = inputFile.readLines()
            val lineCount = lines.size
            var wordCount = 0
            for (line in lines) { wordCount += line.trim().split("\\s+".toRegex()).count { it.isNotEmpty() } }

            val writerFile = FileWriter(outputFilePath)
            writerFile.use {
                writerFile.append("Общее количество строк: $lineCount\n")
                writerFile.append("Общее количество слов: $wordCount")
            }
            return true
        }
        catch (ex: FileNotFoundException) { println("Ошибка: файл не найден - ${ex.message}"); }
        catch (ex: IOException) { println("Ошибка при работе с файлом - ${ex.message}"); }
        catch (ex: Exception) { println("Непредвиденная ошибка - ${ex.message}");  }
        return false
    }
}

/**
 * Реализация через Java NIO
 */
class NIOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        try {
            val lines = Files.readAllLines(Paths.get(inputFilePath))
            val lineCount = lines.size
            var wordCount = 0
            for (line in lines) { wordCount += line.trim().split("\\s+".toRegex()).count { it.isNotEmpty() } }

            Files.writeString(Paths.get(outputFilePath), "Общее количество строк: $lineCount\nОбщее количество слов: $wordCount")
            return true
        }
        catch (ex: FileNotFoundException) { println("Ошибка: файл не найден - ${ex.message}"); }
        catch (ex: IOException) { println("Ошибка при работе с файлом - ${ex.message}"); }
        catch (ex: Exception) { println("Непредвиденная ошибка - ${ex.message}");  }
        return false
    }
}

fun main() {
    val ioFileAnalyzer = IOFileAnalyzer()
    ioFileAnalyzer.countLinesAndWordsInFile(
        "lesson5/src/main/kotlin/ru/tbank/education/school/homework/input.txt",
        "lesson5/src/main/kotlin/ru/tbank/education/school/homework/ioOutput.txt"
    )

    val nioFileAnalyzer = NIOFileAnalyzer()
    nioFileAnalyzer.countLinesAndWordsInFile(
        "lesson5/src/main/kotlin/ru/tbank/education/school/homework/input.txt",
        "lesson5/src/main/kotlin/ru/tbank/education/school/homework/nioOutput.txt"
    )
}