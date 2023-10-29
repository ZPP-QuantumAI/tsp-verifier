package pl.mimuw.zpp.quantumai.tspverifier.model;

import lombok.Builder;
import pl.mimuw.zpp.quantumai.tspverifier.model.graph.WeightedGraph;

@Builder
public record Input(
        WeightedGraph graph,
        int startingVertex
) {
}
