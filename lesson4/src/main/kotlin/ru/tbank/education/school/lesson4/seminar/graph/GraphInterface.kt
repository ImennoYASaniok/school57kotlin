package ru.tbank.education.school.lesson4.seminar.graph

interface InterfaceGraph {
    fun addVertex(number: Int)
    fun addEdge(edge: Pair<Int, Int>)
    fun bfs(start: Int): MutableList<Int>
}


