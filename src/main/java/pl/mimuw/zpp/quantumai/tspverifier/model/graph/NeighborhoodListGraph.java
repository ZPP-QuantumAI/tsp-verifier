package pl.mimuw.zpp.quantumai.tspverifier.model.graph;

import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NeighborhoodListGraph implements AddEdgeWeightedGraph {
    private final int numberOfVertices;
    private final List<List<Edge>> graph;

    public NeighborhoodListGraph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        this.graph = new ArrayList<>();
        for (int i = 0; i <= numberOfVertices; i++) {
            graph.add(new ArrayList<>());
        }
    }

    @Override
    public void addEdge(int a, int b, BigDecimal weight) {
        graph.get(a).add(new Edge(b, weight));
        graph.get(b).add(new Edge(a, weight));
    }

    @Override
    public Either<String, BigDecimal> getWeight(int a, int b) {
        if (verticesAreInvalid(a, b)) return invalidVerticesMessage();
        return graph.get(a).stream()
                .filter(edge -> edge.to == b)
                .findFirst()
                .map(Edge::weight)
                .<Either<String, BigDecimal>>map(Either::right)
                .orElse(Either.left("two vertices do not commute in the graph"));
    }

    @Override
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    private record Edge(
            int to,
            BigDecimal weight
    ) {
    }
}
