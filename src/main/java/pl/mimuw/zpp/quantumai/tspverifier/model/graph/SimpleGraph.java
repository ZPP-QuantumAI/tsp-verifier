package pl.mimuw.zpp.quantumai.tspverifier.model.graph;

import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleGraph implements WeightedGraph {
    private final int numberOfVertices;
    private final List<List<Edge>> graph;

    public SimpleGraph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        this.graph = new ArrayList<>();
        for (int i = 0; i <= numberOfVertices; i++) {
            graph.add(new ArrayList<>());
        }
    }

    public void addEdge(int a, int b, BigDecimal weight) {
        graph.get(a).add(new Edge(b, weight));
        graph.get(b).add(new Edge(a, weight));
    }

    public Either<String, BigDecimal> getWeight(int a, int b) {
        if (!verticesAreValid(a, b))
            return Either.left("vertex number must be less or equal to the number of the number of vertices");
        return graph.get(a).stream()
                .filter(edge -> edge.to == b)
                .findFirst()
                .map(Edge::weight)
                .<Either<String, BigDecimal>>map(Either::right)
                .orElseGet(() -> Either.left("two vertices do not commute in the graph"));
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    private boolean verticesAreValid(int... vertexNumbers) {
        return Arrays.stream(vertexNumbers)
                .filter(vertex -> vertex < 0 || vertex > numberOfVertices)
                .findFirst()
                .isEmpty();
    }

    private record Edge(
            int to,
            BigDecimal weight
    ) {
    }
}
