package pl.mimuw.zpp.quantumai.tspverifier.service

import pl.mimuw.zpp.quantumai.tspverifier.model.Input
import pl.mimuw.zpp.quantumai.tspverifier.model.Output
import pl.mimuw.zpp.quantumai.tspverifier.model.graph.SimpleGraph
import spock.lang.Specification

class VerificationServiceTest extends Specification {
    VerificationService verificationService

    def setup() {
        verificationService = new VerificationService()
    }

    def "should fail when length of output is incorrect"() {
        given:
        def output = Output.builder().permutation([0, 1, 2, 3]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when first or last vertex of permutation is not starting vertex"() {
        given:
        def output = Output.builder().permutation([1, 2, 3, 4, 0, 1]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when permutation contains number smaller than 0"() {
        given:
        def output = Output.builder().permutation([0, 1, 2, -1, 4, 0]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when permutation contains number bigger than number of vertices"() {
        given:
        def output = Output.builder().permutation([0, 1, 2, 5, 4, 0]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when permutation has duplicates on positions other than first and last"() {
        given:
        def output = Output.builder().permutation([0, 1, 2, 1, 4, 0]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should fail when some two vertices do not commute"() {
        given:
        def output = Output.builder().permutation([0, 1, 3, 2, 4, 0]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isLeft()
    }

    def "should correctly calculated the sum of weights"() {
        given:
        def output = Output.builder().permutation([0, 1, 2, 3, 4, 0]).build()

        when:
        def result = verificationService.verifyOutput(testInput, output)

        then:
        result.isRight()
        result.get() == 1 + 2 + 3 + 4 + 5
    }

    def testInput = Input.builder()
            .graph(testGraph())
            .startingVertex(0)
            .build()

    def testGraph() {
        SimpleGraph graph = new SimpleGraph(5)
        graph.addEdge(0, 1, BigDecimal.valueOf(1))
        graph.addEdge(1, 2, BigDecimal.valueOf(2))
        graph.addEdge(2, 3, BigDecimal.valueOf(3))
        graph.addEdge(3, 4, BigDecimal.valueOf(4))
        graph.addEdge(4, 0, BigDecimal.valueOf(5))
        graph.addEdge(0, 2, BigDecimal.valueOf(6))
        graph.addEdge(0, 3, BigDecimal.valueOf(7))
        graph.addEdge(1, 3, BigDecimal.valueOf(8))
        graph.addEdge(1, 4, BigDecimal.valueOf(9))
        return graph
    }
}
