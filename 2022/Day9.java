import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day9 {
  private static Map<Character, Point> diff = Map.of(
    'U', new Point(0, 1),
    'D', new Point(0, -1),
    'R', new Point(1, 0),
    'L', new Point(-1, 0));

  private static void updateTail(Point head, Point tail) {
    if (Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1) {
      tail.x += Integer.signum(head.x - tail.x);
      tail.y += Integer.signum(head.y - tail.y);
    }
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = 10;
    Point[] knots = new Point[n];
    for (int i = 0; i < n; i++) {
      knots[i] = new Point(0, 0);
    }
    Set<String> pos = new HashSet<>();
    pos.add(knots[n-1].toString()); // The last knot is the tail.
    while (sc.hasNext()) {
      char dir = sc.next().charAt(0);
      int steps = sc.nextInt();
      while (steps > 0) {
        knots[0].x += diff.get(dir).x;
        knots[0].y += diff.get(dir).y;
        for (int i = 1; i < n; i++) {
          updateTail(knots[i-1], knots[i]);
        }
        pos.add(knots[n-1].toString());
        steps--;
      }
    }
    System.out.println(pos.size());
  }

  static class Point {
    int x, y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public String toString() {
      return "%d:%d".formatted(x, y);
    }
  }
}
