
package daa;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsTest {

    @Test
    void editDistanceReferenceValues() {

        assertEquals(
                3,
                EditDistance.recursive("kitten", "sitting")
        );

        assertEquals(
                2,
                EditDistance.table("flaw", "lawn")
        );

        assertEquals(
                5,
                EditDistance.twoRows("intention", "execution")
        );

        assertEquals(
                3,
                EditDistance.table("", "abc")
        );

        assertEquals(
                0,
                EditDistance.twoRows("abc", "abc")
        );

        assertEquals(
                0,
                EditDistance.recursive("", "")
        );
    }

    @Test
    void allEditDistanceVersionsAgree() {

        Random random = new Random(12345);

        String alphabet = "abc";

        for (int k = 0; k < 100; k++) {

            String a = randomString(
                    random, alphabet, random.nextInt(8)
            );

            String b = randomString(
                    random, alphabet, random.nextInt(8)
            );

            int expected = EditDistance.table(a, b);

            assertEquals(
                    expected,
                    EditDistance.recursive(a, b)
            );

            assertEquals(
                    expected,
                    EditDistance.twoRows(a, b)
            );
        }
    }

    @Test
    void lpsReferenceArrays() {

        assertArrayEquals(
                new int[]{0, 1, 2, 0, 1, 2, 3, 3},
                StringSearch.lps("AAACAAAA")
        );

        assertArrayEquals(
                new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4},
                StringSearch.lps("ABABCABAB")
        );
    }

    @Test
    void searchReferenceValues() {

        assertEquals(
                List.of(10),
                StringSearch.kmp(
                        "ABABDABACDABABCABAB",
                        "ABABCABAB"
                )
        );

        assertEquals(
                List.of(0, 1, 2, 3),
                StringSearch.naive("AAAAA", "AA")
        );

        assertEquals(
                List.of(0, 2, 4),
                StringSearch.kmp("ABABABA", "ABA")
        );
    }

    @Test
    void naiveAndKmpAgreeOnRandomInputs() {

        Random random = new Random(67890);

        String alphabet = "ABCD";

        for (int k = 0; k < 100; k++) {

            String text = randomString(
                    random, alphabet, random.nextInt(30) + 1
            );

            String pattern = randomString(
                    random, alphabet, random.nextInt(8) + 1
            );

            assertEquals(
                    StringSearch.naive(text, pattern),
                    StringSearch.kmp(text, pattern)
            );
        }
    }

    @Test
    void searchEdgeCases() {

        assertThrows(
                IllegalArgumentException.class,
                () -> StringSearch.naive("abc", "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> StringSearch.kmp("abc", "")
        );

        assertEquals(
                List.of(),
                StringSearch.naive("ab", "abc")
        );

        assertEquals(
                List.of(),
                StringSearch.kmp("ab", "abc")
        );
    }

    private static String randomString(
            Random random, String alphabet, int length) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {

            result.append(
                    alphabet.charAt(
                            random.nextInt(alphabet.length())
                    )
            );
        }

        return result.toString();
    }
}
