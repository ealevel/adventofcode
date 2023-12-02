import java.util.Scanner;
import java.util.Set;

public class Day10 {
  private static Set<Integer> signals = Set.of(20, 60, 100, 140, 180, 220);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int x = 1;
    int cycle = 1;
    char[][] crt = new char[6][40];
    int strength = 0;

    while (sc.hasNext()) {
      String op = sc.next();
      if (op.equals("addx")) {
        int plus = sc.nextInt();
        for (int i = 0; i < 2; i++) {
          int pos = (cycle - 1) % 40;
          if (x - 1 <= pos && pos <= x + 1) {
            crt[(cycle - 1)/40][pos] = '#';
          } else {
            crt[(cycle - 1)/40][pos] = '.';
          }
          if (signals.contains(cycle)) {
            strength += cycle * x;
          }
          cycle++;
        }
        x += plus;
      } else {
        int pos = (cycle - 1) % 40;
        if (x - 1 <= pos && pos <= x + 1) {
          crt[(cycle - 1)/40][pos] = '#';
        } else {
          crt[(cycle - 1)/40][pos] = '.';
        }
        if (signals.contains(cycle)) {
          strength += cycle * x;
        }
        cycle++;
      }
    }

    System.out.println(strength);
    for (char[] row : crt) {
      System.out.println(String.valueOf(row));
    }
  }
}
