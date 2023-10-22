package pl.mimuw.zpp.quantumai.tspverifier.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Output(
        List<Integer> permutation
) {
}
