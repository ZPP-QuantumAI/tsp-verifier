package pl.mimuw.zpp.quantumai.tspverifier.service;

import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.tspverifier.model.Input;
import pl.mimuw.zpp.quantumai.tspverifier.model.graph.*;

import java.math.BigDecimal;
import java.util.Scanner;

@Service
public class InputService {
    public Input generateNeighborhoodListGraph(Scanner scanner) {
        int numberOfVertices = scanner.nextInt();
        int numberOfEdges = scanner.nextInt();
        int startingVertex = scanner.nextInt();

        NeighborhoodListGraph graph = new NeighborhoodListGraph(numberOfVertices);
        AddEdgeWeightedGraph resultGraph = populateGraphByNeighborhoodListInput(scanner, numberOfEdges, graph);

        return Input.builder()
                .startingVertex(startingVertex)
                .graph(resultGraph)
                .build();
    }

    public Input generateMatrixGraph(Scanner scanner) {
        int numberOfVertices = scanner.nextInt();
        int numberOfEdges = scanner.nextInt();
        int startingVertex = scanner.nextInt();

        MatrixGraph graph = new MatrixGraph(numberOfVertices);
        populateGraphByNeighborhoodListInput(scanner, numberOfEdges, graph);

        return Input.builder()
                .startingVertex(startingVertex)
                .graph(graph)
                .build();
    }

    public Input generateEuclideanGraph(Scanner scanner) {
        int numberOfVertices = scanner.nextInt();
        int startingVertex = scanner.nextInt();

        EuclideanGraph graph = new EuclideanGraph();
        for (int i = 0; i < numberOfVertices; i++) {
            BigDecimal x = scanner.nextBigDecimal();
            BigDecimal y = scanner.nextBigDecimal();
            graph.addNode(x, y);
        }

        return Input.builder()
                .startingVertex(startingVertex)
                .graph(graph)
                .build();
    }

    private AddEdgeWeightedGraph populateGraphByNeighborhoodListInput(Scanner scanner, int numberOfEdges, AddEdgeWeightedGraph graph) {
        for (int i = 0; i < numberOfEdges; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            BigDecimal weight = scanner.nextBigDecimal();
            graph.addEdge(a, b, weight);
        }
        return graph;
    }
}
