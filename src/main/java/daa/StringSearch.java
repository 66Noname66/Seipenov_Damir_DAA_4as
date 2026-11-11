
package daa;

import java.util.ArrayList;
import java.util.List;

public class StringSearch {

    public static class SearchResult {
        public final List<Integer> positions;
        public final long comparisons;

        public SearchResult(List<Integer> positions, long comparisons) {
            this.positions = positions;
            this.comparisons = comparisons;
        }
    }

    public static List<Integer> naive(String text, String pattern) {
        return naiveCounted(text, pattern).positions;
    }

    public static SearchResult naiveCounted(
            String text, String pattern) {

        validatePattern(pattern);

        List<Integer> matches = new ArrayList<>();
        long comparisons = 0;

        if (pattern.length() > text.length()) {
            return new SearchResult(matches, comparisons);
        }

        for (int i = 0; i <= text.length() - pattern.length(); i++) {

            int j = 0;

            while (j < pattern.length()) {

                comparisons++;

                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }

                j++;
            }

            if (j == pattern.length()) {
                matches.add(i);
            }
        }

        return new SearchResult(matches, comparisons);
    }

    // Builds the Longest Prefix Suffix array.
    public static int[] lps(String pattern) {

        validatePattern(pattern);

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    public static List<Integer> kmp(String text, String pattern) {
        return kmpCounted(text, pattern).positions;
    }

    public static SearchResult kmpCounted(
            String text, String pattern) {

        validatePattern(pattern);

        List<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return new SearchResult(matches, 0);
        }

        int[] lps = lps(pattern);

        int i = 0;
        int j = 0;

        long comparisons = 0;

        while (i < text.length()) {

            comparisons++;

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {

                    matches.add(i - j);

                    // Continue searching for overlapping matches.
                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {

                    j = lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return new SearchResult(matches, comparisons);
    }

    private static void validatePattern(String pattern) {

        if (pattern.isEmpty()) {
            throw new IllegalArgumentException(
                    "Pattern must not be empty"
            );
        }
    }
}
