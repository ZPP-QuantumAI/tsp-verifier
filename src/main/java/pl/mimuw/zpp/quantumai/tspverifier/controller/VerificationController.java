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
import pl.mimuw.zpp.quantumai.tspverifier.service.InputService;
import pl.mimuw.zpp.quantumai.tspverifier.service.VerificationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static pl.mimuw.zpp.quantumai.tspverifier.utils.Utils.map2;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;
    private final InputService inputService;

    @PostMapping(value = "/verifyNeighborhoodListGraph", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> verifyNeighborhoodListGraph(
            @RequestPart MultipartFile inputFile,
            @RequestPart MultipartFile outputFile
    ) throws Exception {
        return verify(
                () -> generateInput(inputFile, inputService::generateMatrixGraph),
                () -> generateOutput(outputFile)
        );
    }

    @PostMapping(value = "/verifyNeighborhoodListGraphAsMatrix", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> verifyNeighborhoodListGraphAsMatrix(
            @RequestPart MultipartFile inputFile,
            @RequestPart MultipartFile outputFile
    ) throws Exception {
        return verify(
                () -> generateInput(inputFile, inputService::generateMatrixGraph),
                () -> generateOutput(outputFile)
        );
    }

    @PostMapping(value = "/verifyEuclideanGraph", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> verifyEuclideanGraph(
            @RequestPart MultipartFile inputFile,
            @RequestPart MultipartFile outputFile
    ) throws Exception {
        return verify(
                () -> generateInput(inputFile, inputService::generateEuclideanGraph),
                () -> generateOutput(outputFile)
        );
    }

    private ResponseEntity<String> verify(
            Callable<Either<String, Input>> input,
            Callable<Either<String, Output>> output
    ) throws Exception {
        Either<String, BigDecimal> verification = map2(
                input,
                output,
                verificationService::verifyOutput)
                .flatMap(Function.identity());
        return verification.isRight() ? ResponseEntity.ok(verification.get().toString()) : ResponseEntity.badRequest().body(verification.getLeft());
    }

    private Either<String, Input> generateInput(MultipartFile inputFile, Function<Scanner, Input> generator) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile.getInputStream());
            return Either.right(generator.apply(scanner));
        } catch (Exception e) {
            return Either.left("invalid graph");
        } finally {
            if (Objects.nonNull(scanner)) scanner.close();
        }
    }

    private Either<String, Output> generateOutput(MultipartFile outputFile) {
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
