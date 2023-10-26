package pl.mimuw.zpp.quantumai.tspverifier.model.graph;

import io.vavr.control.Either;

import java.math.BigDecimal;

public interface WeightedGraph {
    int getNumberOfVertices();
    Either<String, BigDecimal> getWeight(int a, int b);
}
