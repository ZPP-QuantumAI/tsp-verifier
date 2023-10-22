package pl.mimuw.zpp.quantumai.tspverifier.service

import pl.mimuw.zpp.quantumai.tspverifier.model.Graph
import pl.mimuw.zpp.quantumai.tspverifier.model.Input
import pl.mimuw.zpp.quantumai.tspverifier.model.Output
import spock.lang.Specification

class VerificationServiceTest extends Specification {
    VerificationService verificationService

    def setup() {
        verificationService = new VerificationService()
    }

    def "should fail when length of output is incorrect"() {
        given:
        def output = Output.builder().permutation([1, 2, 3, 4]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when first or last vertex of permutation is not starting vertex"() {
        given:
        def output = Output.builder().permutation([2, 3, 4, 5, 1, 2]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when permutation contains number smaller than 1"() {
        given:
        def output = Output.builder().permutation([1, 2, 3, 0, 5, 1]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when permutation contains number bigger than number of vertices"() {
        given:
        def output = Output.builder().permutation([1, 2, 3, 6, 5, 1]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when permutation has duplicates on positions other than first and last"() {
        given:
        def output = Output.builder().permutation([1, 2, 3, 2, 5, 1]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when some two vertices do not commute"() {
        given:
        def output = Output.builder().permutation([1, 2, 4, 3, 5, 1]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should correctly calculated the sum of weights"() {
        given:
        def output = Output.builder().permutation([1, 2, 3, 4, 5, 1]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isRight()
        result.get() == 1 + 2 + 3 + 4 + 5
    }

    def testInput = Input.builder()
            .graph(testGraph())
            .startingVertex(1)
            .build()

    def testGraph() {
        Graph graph = new Graph(5)
        graph.addEdge(1, 2, 1)
        graph.addEdge(2, 3, 2)
        graph.addEdge(3, 4, 3)
        graph.addEdge(4, 5, 4)
        graph.addEdge(5, 1, 5)
        graph.addEdge(1, 3, 6)
        graph.addEdge(1, 4, 7)
        graph.addEdge(2, 4, 8)
        graph.addEdge(2, 5, 9)
        return graph
    }
}
