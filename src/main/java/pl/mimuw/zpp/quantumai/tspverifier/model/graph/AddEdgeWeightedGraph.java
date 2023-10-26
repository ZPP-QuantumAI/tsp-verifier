package pl.mimuw.zpp.quantumai.tspverifier.model.graph;

import java.math.BigDecimal;

public interface AddEdgeWeightedGraph extends WeightedGraph {
    void addEdge(int a, int b, BigDecimal weight);
}
