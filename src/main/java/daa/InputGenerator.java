
package daa;

import java.util.Random;

public class InputGenerator {

    public static String randomString(int length, long seed) {

        Random random = new Random(seed);

        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = random.nextInt(alphabet.length());

            result.append(alphabet.charAt(index));
        }

        return result.toString();
    }

    public static String repeated(char character, int length) {

        return String.valueOf(character).repeat(length);
    }
}
