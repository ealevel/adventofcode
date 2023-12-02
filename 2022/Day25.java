import java.util.Scanner;

public class Day25 {
  private static long[] only1 = new long[20];
  private static long[] only2 = new long[20];

  static {
    long b = 1;
    for (int i = 0; i < 20; i++) {
      only1[i] = b;
      only2[i] = 2 * b;
      b *= 5;
    }
  }

  private static long toNum(String snafu) {
    long n = 0;
    long b = 1;
    for (int d = 0; d < snafu.length(); d++) {
      char digit = snafu.charAt(snafu.length() - 1 - d);
      if (digit == '1') {
        n += b;
      } else if (digit == '2') {
        n += 2 * b;
      } else if (digit == '-') {
        n -= b;
      } else if (digit == '=') {
        n -= 2 * b;
      }
      b *= 5;
    }
    return n;
  }

  private static String toSnafu(long num) {
    String snafu = "";
    for (int i = 19; i >= 0; i--) {
      char digit = '0';
      long best = num;
      // Basically picks a number (one of: =,-,1,2) that brings /num/ closer to 0 in each iteration if possible,
      // otherwise picks '0' and keeps going.
      if (Math.abs(num - only2[i]) < Math.abs(best)) {
        digit = '2';
        best = num - only2[i];
      }
      if (Math.abs(num + only2[i]) < Math.abs(best)) {
        digit = '=';
        best = num + only2[i];
      }
      if (Math.abs(num - only1[i]) < Math.abs(best)) {
        digit = '1';
        best = num - only1[i];
      }
      if (Math.abs(num + only1[i]) < Math.abs(best)) {
        digit = '-';
        best = num + only1[i];
      }
      if (snafu.isEmpty() && digit == '0') {
        // If snafu is empty, then don't add '0' to the front.
        continue;
      }
      snafu += digit;
      num = best;
    }
    if (snafu.isEmpty()) {
      return "0";
    }
    return snafu;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    long s = 0;
    while (sc.hasNext()) {
      long n = toNum(sc.next());
      s += n;
//      System.out.println("%10s %10d".formatted(toSnafu(n), n));
    }
    System.out.println(s);
    System.out.println(toSnafu(s));
    System.out.println(toNum(toSnafu(s)));
  }
}
