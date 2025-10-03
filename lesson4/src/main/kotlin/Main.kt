package ru.tbank.education.school

import ru.tbank.education.school.lesson4.seminar.graph.Graph

fun main() {
    val graph = Graph()
    graph.addEdge(Pair(1, 2))
    graph.addEdge(Pair(1, 3))
    println("AdjacencyMatrix:"); graph.getAdjacencyMatrix(); println()
    println("ListVertex:"); graph.getListsVertexs(); println()
    println("ListVertex:"); graph.getListEdge(); println()
    println("bfs (start = 1):"); println(graph.bfs(1))
}