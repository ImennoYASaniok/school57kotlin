package ru.tbank.education.school.lesson8.homework.library

import kotlin.collections.set

const val DAYS_OVERDUE = 10

class LibraryService {
    private val books = mutableMapOf<String, Book>()
    private val borrowedBooks = mutableSetOf<String>()
    private val borrowerFines = mutableMapOf<String, Int>()
    private val borrowerBooks = mutableMapOf<String, MutableList<String>>()

    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    fun borrowBook(isbn: String, borrower: String, daysOverdue: Int) {
        if (!books.containsKey(isbn)) {
            throw IllegalArgumentException()
        }
        if (!isAvailable(isbn)) {
            throw IllegalArgumentException()
        }

        if (!borrowerBooks.containsKey(borrower)) {
            borrowerBooks[borrower] = mutableListOf(isbn)
        }
        else {
            for (isbn in borrowerBooks[borrower]!!) {
                if (calculateOverdueFine(isbn, daysOverdue) > 0) {
                    throw IllegalArgumentException()
                }
            }
            borrowerBooks[borrower]!!.add(isbn)
        }
        borrowedBooks.add(isbn)
    }

    fun returnBook(isbn: String, borrower: String? = null) {
        if (isAvailable(isbn)) {
            throw IllegalArgumentException()
        }
        else {
            borrowedBooks.remove(isbn)
            if (borrower != null && borrowerBooks.containsKey(borrower)) {
                borrowerBooks[borrower]!!.remove(isbn)
            }
        }
    }

    fun isAvailable(isbn: String): Boolean {
        return !borrowedBooks.contains(isbn)
    }

    fun calculateOverdueFine(isbn: String, daysOverdue: Int): Int {
        if (!borrowedBooks.contains(isbn) || daysOverdue < DAYS_OVERDUE) {
            return 0
        }
        return (daysOverdue - DAYS_OVERDUE) * 60
    }

    private fun hasOutstandingFines(borrower: String): Boolean {
        return (borrowerFines[borrower] ?: 0) > 0
    }
}