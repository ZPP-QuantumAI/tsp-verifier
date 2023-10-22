package pl.mimuw.zpp.quantumai.tspverifier.model;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Graph {
    private final int numberOfVertices;
    private final List<List<Edge>> graph;

    public Graph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        this.graph = new ArrayList<>();
        for (int i = 0; i <= numberOfVertices; i++) {
            graph.add(new ArrayList<>());
        }
    }

    public void addEdge(int a, int b, long weight) {
        validateVertexNumber(a, b);
        graph.get(a).add(new Edge(b, weight));
        graph.get(b).add(new Edge(a, weight));
    }

    public Optional<Long> getWeight(int a, int b) {
        validateVertexNumber(a, b);
        return graph.get(a).stream()
                .filter(edge -> edge.to == b)
                .findFirst()
                .map(Edge::weight);
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    private void validateVertexNumber(int... vertexNumbers) {
        for (int vertexNumber : vertexNumbers) {
            Assert.isTrue(
                    vertexNumber <= numberOfVertices,
                    "vertex number must be less or equal to the number of the number of vertices"
            );
        }
    }

    private record Edge(
            int to,
            long weight
    ) {
    }
}
