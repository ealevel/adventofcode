import java.util.*;
import java.util.regex.MatchResult;

public class Day15 {
  private static int REPORTY = 2_000_000;
  private static int MAXY = 4_000_000;

  private static class Range implements Comparable<Range> {
    int from, to;

    Range(int from, int to) {
      this.from = from;
      this.to = to;
    }

    @Override
    public int compareTo(Range that) {
      if (this.from == that.from) {
        return Integer.compare(this.to, that.to);
      }
      return Integer.compare(this.from, that.from);
    }

    public String toString() {
      return "[%d, %d]".formatted(from, to);
    }
  }

  private static List<Range> mergeOverlapping(List<Range> ranges) {
    Collections.sort(ranges);
    List<Range> merged = new ArrayList<>();
    Range last;
    for (Range r : ranges) {
      if (merged.isEmpty()) {
        merged.add(r);
      } else if (r.from <= (last = merged.get(merged.size()-1)).to + 1) {
        last.to = Math.max(r.to, last.to);
      } else {
        merged.add(r);
      }
    }
    return merged;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Map<Integer, Character> m = new HashMap<>();
    Map<Integer, List<Range>> ranges = new HashMap<>();
    while (sc.hasNextLine()) {
      sc.findInLine("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
      MatchResult result = sc.match();
      int sx = Integer.parseInt(result.group(1));
      int sy = Integer.parseInt(result.group(2));
      int bx = Integer.parseInt(result.group(3));
      int by = Integer.parseInt(result.group(4));
      sc.nextLine(); // Read remaining of line

      int dist = Math.abs(sx - bx) + Math.abs(sy - by); // manhattan distance from S to B

      // Part 1
      if (sy == REPORTY) {
        m.put(sx, 'S');
      }
      if (by == REPORTY) {
        m.put(bx, 'B');
      }

      // This is how much we have to travel for in the X axis
      int xdiff = dist - Math.abs(REPORTY - sy);
      for (int x = sx - xdiff; x <= sx + xdiff; x++) {
        m.putIfAbsent(x, '#');
      }

      // Part 2
      // This adds a range [x0, x1] for each row Y in [0, 4_000_000] based on the
      // *remaining* Manhattan distance after going up or down from S to B.
      for (int y = Math.max(0, sy - dist); y <= Math.min(MAXY, sy + dist); y++) {
        ranges.putIfAbsent(y, new ArrayList<>());
        xdiff = dist - Math.abs(y - sy);
        int xfrom = Math.max(0, sx-xdiff); // Report only from 0, inclusive
        int xto = Math.min(MAXY, sx+xdiff); // Report only to MAXY, inclusive
        ranges.get(y).add(new Range(xfrom, xto));
      }
    }

    // Part 1
    int cnt = 0;
    for (Map.Entry<Integer, Character> e : m.entrySet()) {
      if (e.getValue() == '#') {
        cnt++;
      }
    }
    System.out.println("Part 1: " + cnt);

    // Part 2
    for (int y = 0; y <= MAXY; y++) {
      List<Range> merged = mergeOverlapping(ranges.get(y));
      if (merged.size() > 1) {
        System.out.println("Part 2: x=%d y=%d".formatted(merged.get(0).to+1, y));
        System.out.println(Arrays.toString(ranges.get(y).toArray()));
        System.out.println(Arrays.toString(merged.toArray()));
      }
    }
  }
}
