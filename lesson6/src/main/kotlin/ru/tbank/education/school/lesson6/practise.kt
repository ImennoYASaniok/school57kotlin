package ru.tbank.education.school.lesson6

fun main() {
    task1Lists()
    task2Sets()
    task3Maps()
    task4FilterAndGroup()
    task5Books()
    task6Store()
    task7Students()
}

// -----------------------
// 1. Работа со списками
// -----------------------
fun task1Lists() {
    println("=== Task 1: Lists ===")
    val shoppingList = listOf("Молоко", "Хлеб", "Яблоки", "Сыр")

    // TODO: проверить, есть ли "Хлеб" в списке
    println(shoppingList.contains("Хлеб"))

    // TODO: отсортировать список по алфавиту и вывести
    println(shoppingList.sorted())

    // TODO: вывести только товары, начинающиеся на букву "С"
    println(shoppingList.filter { it[0] == 'С' })
}

// -----------------------
// 2. Работа с множествами
// -----------------------
fun task2Sets() {
    println("=== Task 2: Sets ===")
    val kotlinStudents = mutableSetOf("Анна", "Иван", "Мария")
    val javaStudents = setOf("Иван", "Петр", "Ольга")

    // TODO: добавить дубликат в kotlinStudents и посмотреть, что произойдет
    kotlinStudents.add("Анна"); println(kotlinStudents) // Ничего не изменится

    // TODO: проверить, есть ли "Мария" в списке студентов Kotlin
    println(kotlinStudents.contains("Мария"))

    // TODO: найти пересечение студентов Kotlin и Java курсов
    println(kotlinStudents.filter { it in javaStudents })
}

// -----------------------
// 3. Работа с Map
// -----------------------
fun task3Maps() {
    println("=== Task 3: Maps ===")
    val products = mutableMapOf(
        "Телефон" to 50000,
        "Ноутбук" to 80000,
        "Наушники" to 3000
    )

    products["Телефон"] = 52000; println(products)
    products["Планшет"] = 40000; println(products)
    products.filter { it.value > 1000 }
    println(products["werwere"])

    // TODO: изменить цену для "Телефон"
    // TODO: добавить новый товар "Планшет" с ценой 40000
    // TODO: вывести только товары дороже 10000
    // TODO: обработать случай, когда пользователь запрашивает отсутствующий товар
}

// -----------------------
// 4. Фильтрация и группировка
// -----------------------
fun task4FilterAndGroup() {
    println("=== Task 4: Filter & Group ===")
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    print("Чётные:"); println(numbers.filter { it % 2 == 0 })
    print("Нечётные:"); println(numbers.filter { it % 2 == 0 })
    println(numbers.map { "Число ${it}" })

    // TODO: сгруппировать числа на четные и нечетные
    // TODO: преобразовать список чисел в список строк "Число: X"

    val words = listOf("кот", "пес", "кот", "лиса", "пес")

    // TODO: выделить только уникальные слова

    println(words.toSet()) // words.filter { it1 -> (words.filter { it2 -> it2 == it1 }).size == 1 } // Повторяются О(n)
}

// -----------------------
// 5. Каталог книг
// -----------------------
fun task5Books() {
    println("=== Task 5: Books ===")
    data class Book(val title: String, val author: String, val year: Int)

    val books = listOf(
        Book("Kotlin в действии", "Иванов", 2019),
        Book("Программирование на Java", "Петров", 2018),
        Book("Современный Kotlin", "Иванов", 2021),
    )

    // TODO: найти все книги автора "Иванов"
    println(books.filter { it.author == "Иванов" })

    // TODO: отсортировать книги по году
    println(books.sortedBy { it.year })

    // TODO: сгруппировать книги по авторам
    books.groupBy { it.author }
    val a = books.map { it.author }.toSet()
    println(a.map { it1 -> books.filter { it2 -> it1 == it2.author } })
}

// -----------------------
// 6. Магазин
// -----------------------
fun task6Store() {
    println("=== Task 6: Store ===")
    val store = mapOf(
        "Молоко" to 70,
        "Хлеб" to 40,
        "Яблоки" to 100
    )
    val cart = listOf("Молоко", "Хлеб", "Сыр")

    // TODO: посчитать общую стоимость покупок
    println(store.values.sum())

    // TODO: вывести сообщение для товаров, которых нет в магазине
    // for (it in cart.filter { !(it in store.keys) }) throw Exception("Ошибка: ${it} не в магазине") // Exception
    for (it in cart.filter { !(it in store.keys) }) println("Ошибка: ${it} не в магазине")
}

// -----------------------
// 7. Студенты
// -----------------------
fun task7Students() {
    println("=== Task 7: Students ===")
    data class Student(val name: String, val group: String)

    val kotlinCourse = setOf(
        Student("Анна", "101"),
        Student("Иван", "102"),
        Student("Мария", "101")
    )

    val javaCourse = setOf(
        Student("Иван", "102"),
        Student("Петр", "103"),
        Student("Ольга", "104")
    )

    // TODO: найти студентов, которые учатся и на Kotlin, и на Java
    val javaStudentNames = javaCourse.map { it.name }
    println(kotlinCourse.map { it.name }.filter { it in javaStudentNames } )

    // TODO: сгруппировать студентов Kotlin по группам
    println(kotlinCourse.map { it1 -> kotlinCourse.filter { it2 -> it1.group == it2.group } }.toSet())
}
