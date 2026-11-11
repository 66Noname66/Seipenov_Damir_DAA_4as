
package daa;

public class EditDistance {

    public static class Result {
        public final int distance;
        public final long operations;

        public Result(int distance, long operations) {
            this.distance = distance;
            this.operations = operations;
        }
    }


    public static int recursive(String a, String b) {
        return recursiveCounted(a, b).distance;
    }

    public static Result recursiveCounted(String a, String b) {
        long[] calls = {0};

        int answer = recursiveHelper(
                a, b, a.length(), b.length(), calls
        );

        return new Result(answer, calls[0]);
    }

    private static int recursiveHelper(
            String a, String b,
            int i, int j,
            long[] calls) {

        calls[0]++;

        if (i == 0) return j;
        if (j == 0) return i;

        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return recursiveHelper(a, b, i - 1, j - 1, calls);
        }

        int delete = recursiveHelper(a, b, i - 1, j, calls);
        int insert = recursiveHelper(a, b, i, j - 1, calls);
        int replace = recursiveHelper(a, b, i - 1, j - 1, calls);

        return 1 + Math.min(
                delete,
                Math.min(insert, replace)
        );
    }


    public static int table(String a, String b) {
        return tableCounted(a, b).distance;
    }

    public static Result tableCounted(String a, String b) {

        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        long operations = 0;

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost = a.charAt(i - 1) == b.charAt(j - 1)
                        ? 0 : 1;

                dp[i][j] = Math.min(
                        dp[i - 1][j] + 1,
                        Math.min(
                                dp[i][j - 1] + 1,
                                dp[i - 1][j - 1] + cost
                        )
                );

                operations++;
            }
        }

        return new Result(dp[n][m], operations);
    }


    public static int twoRows(String a, String b) {
        return twoRowsCounted(a, b).distance;
    }

    public static Result twoRowsCounted(String a, String b) {

        // Keep the shorter string in the columns.
        if (b.length() > a.length()) {
            String temp = a;
            a = b;
            b = temp;
        }

        int n = a.length();
        int m = b.length();

        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            previous[j] = j;
        }

        long operations = 0;

        for (int i = 1; i <= n; i++) {

            current[0] = i;

            for (int j = 1; j <= m; j++) {

                int cost = a.charAt(i - 1) == b.charAt(j - 1)
                        ? 0 : 1;

                current[j] = Math.min(
                        previous[j] + 1,
                        Math.min(
                                current[j - 1] + 1,
                                previous[j - 1] + cost
                        )
                );

                operations++;
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return new Result(previous[m], operations);
    }
}
