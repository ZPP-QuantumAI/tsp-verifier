package pl.mimuw.zpp.quantumai.tspverifier.controller;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.tspverifier.model.Input;
import pl.mimuw.zpp.quantumai.tspverifier.model.Output;
import pl.mimuw.zpp.quantumai.tspverifier.model.graph.NeighborhoodListGraph;
import pl.mimuw.zpp.quantumai.tspverifier.service.VerificationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

import static pl.mimuw.zpp.quantumai.tspverifier.utils.Utils.map2;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> verify(
            @RequestPart MultipartFile inputFile,
            @RequestPart MultipartFile outputFile
    ) throws Exception {
        Either<String, BigDecimal> verification = map2(
                    () -> generateInput(inputFile),
                    () -> generateOutput(outputFile),
                    verificationService::verifyOutput)
                .flatMap(Function.identity());
        return verification.isRight() ? ResponseEntity.ok(verification.get().toString()) : ResponseEntity.badRequest().body(verification.getLeft());
    }

    public static Either<String, Input> generateInput(MultipartFile inputFile) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile.getInputStream());
            int numberOfVertices = scanner.nextInt();
            int numberOfEdges = scanner.nextInt();
            int startingVertex = scanner.nextInt();

            NeighborhoodListGraph graph = new NeighborhoodListGraph(numberOfVertices);
            for (int i = 0; i < numberOfEdges; i++) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                BigDecimal weight = scanner.nextBigDecimal();
                graph.addEdge(a, b, weight);
            }

            return Either.right(
                    Input.builder()
                            .startingVertex(startingVertex)
                            .graph(graph)
                            .build()
            );
        } catch (Exception e) {
            return Either.left("invalid graph");
        } finally {
            if (Objects.nonNull(scanner)) scanner.close();
        }
    }

    public static Either<String, Output> generateOutput(MultipartFile outputFile) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(outputFile.getInputStream());
            List<Integer> permutation = new ArrayList<>();
            while (scanner.hasNextInt()) {
                permutation.add(scanner.nextInt());
            }
            return Either.right(
                    Output.builder()
                            .permutation(permutation)
                            .build()
            );
        } catch (Exception e) {
            return Either.left("invalid output");
        } finally {
            if (Objects.nonNull(scanner)) scanner.close();
        }
    }
}
