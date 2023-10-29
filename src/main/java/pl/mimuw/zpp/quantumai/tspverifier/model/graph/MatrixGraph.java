package pl.mimuw.zpp.quantumai.tspverifier.model.graph;

import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.Optional;

public class MatrixGraph implements AddEdgeWeightedGraph {
    private final BigDecimal[][] matrix;

    public MatrixGraph(int numberOfVertices) {
        this.matrix = new BigDecimal[numberOfVertices][numberOfVertices];
    }

    @Override
    public void addEdge(int a, int b, BigDecimal weight) {
        matrix[a][b] = weight;
        matrix[b][a] = weight;
    }

    @Override
    public int getNumberOfVertices() {
        return matrix.length;
    }

    @Override
    public Either<String, BigDecimal> getWeight(int a, int b) {
        if (verticesAreInvalid(a, b)) return invalidVerticesMessage();
        return Optional.ofNullable(matrix[a][b])
                .<Either<String, BigDecimal>>map(Either::right)
                .orElse(Either.left("two vertices do not commute in the graph"));
    }
}
