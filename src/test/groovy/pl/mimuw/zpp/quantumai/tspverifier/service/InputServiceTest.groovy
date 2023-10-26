package pl.mimuw.zpp.quantumai.tspverifier.service

import pl.mimuw.zpp.quantumai.tspverifier.model.Input
import spock.lang.Specification

import java.math.MathContext

import static java.math.BigDecimal.valueOf

class InputServiceTest extends Specification {
    InputService inputService
    Scanner scanner

    def setup() {
        inputService = new InputService()
    }

    def cleanup() {
        if (scanner != null) scanner.close()
    }

    def shouldGenerateNeighborhoodListGraph() {
        given:
        scanner = new Scanner(simpleNeighbourhoodListInput())

        when:
        def result = inputService.generateNeighborhoodListGraph(scanner)

        then:
        inputMatchesSimpleNeighbourhoodListInput(result)
    }

    def shouldGenerateMatrixGraph() {
        given:
        scanner = new Scanner(simpleNeighbourhoodListInput())

        when:
        def result = inputService.generateMatrixGraph(scanner)

        then:
        inputMatchesSimpleNeighbourhoodListInput(result)
    }

    def shouldGenerateEuclideanGraph() {
        given:
        scanner = new Scanner(simpleEuclideanInput())

        when:
        def result = inputService.generateEuclideanGraph(scanner)

        then:
        inputMatchesSimpleEuclideanInput(result)
    }

    def inputMatchesSimpleEuclideanInput(Input input) {
        def res = true
        res &= input.startingVertex() == 1
        def graph = input.graph()
        res &= graph.getNumberOfVertices() == 4
        res &= graph.getWeight(0, 1).get() == valueOf(1)
        res &= graph.getWeight(0, 2).get() == valueOf(2).sqrt(MathContext.DECIMAL128)
        res &= graph.getWeight(0, 3).get() == valueOf(1)
        res &= graph.getWeight(1, 2).get() == valueOf(1)
        res &= graph.getWeight(1, 3).get() == valueOf(2).sqrt(MathContext.DECIMAL128)
        res &= graph.getWeight(2, 3).get() == valueOf(1)
        return res
    }

    def inputMatchesSimpleNeighbourhoodListInput(Input input) {
        def res = true
        res &= input.startingVertex() == 1
        def graph = input.graph()
        res &= graph.getNumberOfVertices() == 5
        res &= graph.getWeight(0, 2).get() == valueOf(6)
        res &= graph.getWeight(0, 3).get() == valueOf(7)
        res &= graph.getWeight(0, 4).get() == valueOf(5)
        res &= graph.getWeight(1, 2).get() == valueOf(2)
        res &= graph.getWeight(1, 3).get() == valueOf(8)
        res &= graph.getWeight(1, 4).get() == valueOf(9)
        res &= graph.getWeight(2, 3).get() == valueOf(3)
        res &= graph.getWeight(2, 4).isLeft()
        res &= graph.getWeight(3, 4).get() == valueOf(4)
        return res
    }

    def simpleEuclideanInput() {
        return """
            4 1
            0 0
            1 0
            1 1
            0 1
        """
    }

    def simpleNeighbourhoodListInput() {
        return """
            5 9 1
            0 1 1
            1 2 2
            2 3 3
            3 4 4
            4 0 5
            0 2 6
            0 3 7
            1 3 8
            1 4 9
        """
    }
}
