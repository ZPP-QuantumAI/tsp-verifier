package pl.mimuw.zpp.quantumai.tspverifier.model;

import lombok.Builder;

@Builder
public record Input(
        Graph graph,
        int startingVertex
) {
}
