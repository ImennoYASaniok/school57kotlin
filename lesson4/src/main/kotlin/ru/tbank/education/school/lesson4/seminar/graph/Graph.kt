package ru.tbank.education.school.ru.tbank.education.school.lesson4.seminar.graph

import ru.tbank.education.school.lesson4.seminar.graph.InterfaceGraph

open class Graph(): InterfaceGraph {
    override fun addVertex(number: Int) { TODO() }
    override fun addEdge(edge: Pair<Int, Int>) { TODO() }
    override fun bfs(start: Int): MutableList<Int> { TODO() }
}

class GraphAdjacencyMatrix(val adjacencyMatrix: MutableList<MutableList<Boolean>>): Graph() {
    fun getGraph(): MutableList<MutableList<Boolean>> { return adjacencyMatrix }
}


class GraphListsVertexs(val listsVertexs: MutableMap<Int, MutableList<Int>>): Graph() {
    fun getGraph(): MutableMap<Int, MutableList<Int>> { return listsVertexs }
}

class GraphListEdge(val listEdge: MutableList<Pair<Int, Int>>): Graph() {
    fun getGraph(): MutableList<Pair<Int, Int>> { return listEdge }
}