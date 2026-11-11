
package daa;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ExperimentRunner {

    private static final long SEED = 20261107L;
    private static final int REPEATS = 5;

    public static void main(String[] args) throws IOException {

        Files.createDirectories(Paths.get("results"));

        List<String> rows = new ArrayList<>();

        rows.add(
                "algorithm,input,n,pattern_length,time_ms,counter,answer"
        );

        runEdit(rows, "recursive",
                new int[]{4, 6, 8, 10});

        runEdit(rows, "table",
                new int[]{100, 200, 400, 800});

        runEdit(rows, "two_rows",
                new int[]{100, 200, 400, 800});

        runSearch(rows, "naive",
                new int[]{10000, 20000, 40000, 80000});

        runSearch(rows, "kmp",
                new int[]{10000, 20000, 40000, 80000});

        Files.write(
                Paths.get("results/results.csv"),
                rows
        );

        System.out.println("Experiment completed!");
        System.out.println("Results saved to results/results.csv");
    }

    private static void runEdit(
            List<String> rows, String algorithm, int[] sizes) {

        for (int n : sizes) {

            String a = InputGenerator.randomString(
                    n, SEED + n
            );

            String b = InputGenerator.randomString(
                    n, SEED + n + 1
            );

            long[] times = new long[REPEATS];

            EditDistance.Result result = null;

            for (int r = 0; r < REPEATS; r++) {

                long start = System.nanoTime();

                if (algorithm.equals("recursive")) {

                    result = EditDistance.recursiveCounted(a, b);

                } else if (algorithm.equals("table")) {

                    result = EditDistance.tableCounted(a, b);

                } else {

                    result = EditDistance.twoRowsCounted(a, b);
                }

                times[r] = System.nanoTime() - start;
            }

            double timeMs = median(times) / 1_000_000.0;

            rows.add(String.format(
                    Locale.US,
                    "%s,random_pair,%d,0,%.6f,%d,%d",
                    algorithm,
                    n,
                    timeMs,
                    result.operations,
                    result.distance
            ));

            System.out.println(
                    algorithm + " n=" + n +
                            " time=" + timeMs + " ms"
            );
        }
    }

    private static void runSearch(
            List<String> rows, String algorithm, int[] sizes) {

        int patternLength = 64;

        for (int n : sizes) {

            String text = InputGenerator.repeated('A', n);

            String[] patterns = {
                    "A".repeat(patternLength - 1) + "B",
                    "A".repeat(patternLength)
            };

            String[] types = {
                    "repetitive_worst",
                    "repetitive_match"
            };

            for (int k = 0; k < patterns.length; k++) {

                String pattern = patterns[k];

                long[] times = new long[REPEATS];

                StringSearch.SearchResult result = null;

                for (int r = 0; r < REPEATS; r++) {

                    long start = System.nanoTime();

                    if (algorithm.equals("naive")) {

                        result = StringSearch.naiveCounted(
                                text, pattern
                        );

                    } else {

                        result = StringSearch.kmpCounted(
                                text, pattern
                        );
                    }

                    times[r] = System.nanoTime() - start;
                }

                double timeMs = median(times) / 1_000_000.0;

                rows.add(String.format(
                        Locale.US,
                        "%s,%s,%d,%d,%.6f,%d,%d",
                        algorithm,
                        types[k],
                        n,
                        patternLength,
                        timeMs,
                        result.comparisons,
                        result.positions.size()
                ));

                System.out.println(
                        algorithm + " " + types[k] +
                                " n=" + n +
                                " comparisons=" + result.comparisons
                );
            }
        }
    }

    private static long median(long[] values) {

        long[] copy = values.clone();

        Arrays.sort(copy);

        return copy[copy.length / 2];
    }
}
