package pl.mimuw.zpp.quantumai.tspverifier.service;

import io.vavr.control.Either;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.tspverifier.model.Graph;
import pl.mimuw.zpp.quantumai.tspverifier.model.Input;
import pl.mimuw.zpp.quantumai.tspverifier.model.Output;

import java.util.List;
import java.util.Optional;

@Service
public class VerificationService {
    public Either<String, Long> verifyOutput(Input input, Output output) {
        List<Integer> permutation = output.permutation();
        if (!hasMatchingSize(input, output)) {
            return Either.left("output's length should be equal to number of vertices plus one");
        }
        if (!permutation.get(0).equals(input.startingVertex())) {
            return Either.left("the first vertex should be starting vertex");
        }
        if (!permutation.get(permutation.size() - 1).equals(input.startingVertex())) {
            return Either.left("the last vertex should be starting vertex");
        }
        if (hasInvalidVertexNumbers(permutation, input.graph().getNumberOfVertices())) {
            return Either.left("the permutation should have vertex numbers in range [1, max vertex number]");
        }
        if (hasDuplicates(permutation)) {
            return Either.left("the permutation contains duplicates");
        }

        Graph graph = input.graph();
        long result = 0;

        for (int i = 0; i < permutation.size() - 1; i++) {
            Optional<Long> weight = graph.getWeight(permutation.get(i), permutation.get(i + 1));
            if (weight.isPresent()) {
                result += weight.get();
            } else {
                return Either.left("some two vertices in the permutation do not commute");
            }
        }

        return Either.right(result);
    }

    private static boolean hasMatchingSize(Input input, Output output) {
        return output.permutation().size() == input.graph().getNumberOfVertices() + 1;
    }

    private static boolean hasInvalidVertexNumbers(List<Integer> permutation, int maxVertexNumber) {
        return permutation.stream().anyMatch(elem -> elem < 1 || elem > maxVertexNumber);
    }

    private static boolean hasDuplicates(List<Integer> permutation) {
        boolean[] usedNumbers = new boolean[permutation.size()];
        for (int i = 0; i < permutation.size() - 1; i++) {
            if (usedNumbers[permutation.get(i)]) {
                return true;
            }
            usedNumbers[permutation.get(i)] = true;
        }
        return false;
    }
}
