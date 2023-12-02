import java.util.*;

public class Day23 {
  enum Dir {
    NORTH, SOUTH, WEST, EAST;
    Dir next() {
      switch (this) {
        case NORTH: return SOUTH;
        case SOUTH: return WEST;
        case WEST: return EAST;
        case EAST: return NORTH;
      }
      throw new IllegalArgumentException();
    }
  }
  record Point(int x, int y) {}

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Set<Point> points = new HashSet<>();
    for (int y = 0; sc.hasNext(); y++) {
      String line = sc.next();
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          Point p = new Point(x, y);
          points.add(p);
        }
      }
    }

    Dir nextDir = Dir.NORTH;
    int move = 0;
    while (true) {
      Map<Point, Point> proposals = new HashMap<>();
      Set<Point> proposalValues = new HashSet<>();
      Set<Point> repeated = new HashSet<>();
      for (Point p : points) {
        Point newP;
        for (Dir d : new Dir[]{nextDir, nextDir.next(), nextDir.next().next(), nextDir.next().next().next()}) {
          if (!points.contains(new Point(p.x - 1, p.y - 1)) &&
              !points.contains(new Point(p.x, p.y - 1)) &&
              !points.contains(new Point(p.x + 1, p.y - 1)) &&

              !points.contains(new Point(p.x - 1, p.y)) &&
              !points.contains(new Point(p.x + 1, p.y)) &&

              !points.contains(new Point(p.x - 1, p.y + 1)) &&
              !points.contains(new Point(p.x, p.y + 1)) &&
              !points.contains(new Point(p.x + 1, p.y + 1))) {
            // We don't need to move this elf.
            break;
          } else if (d == Dir.NORTH &&
            !points.contains(new Point(p.x - 1, p.y - 1)) &&
            !points.contains(new Point(p.x, p.y - 1)) &&
            !points.contains(new Point(p.x + 1, p.y - 1))) {
            newP = new Point(p.x, p.y - 1);
          } else if (d == Dir.SOUTH &&
            !points.contains(new Point(p.x - 1, p.y + 1)) &&
            !points.contains(new Point(p.x, p.y + 1)) &&
            !points.contains(new Point(p.x + 1, p.y + 1))) {
            newP = new Point(p.x, p.y + 1);
          } else if (d == Dir.WEST &&
            !points.contains(new Point(p.x - 1, p.y - 1)) &&
            !points.contains(new Point(p.x - 1, p.y)) &&
            !points.contains(new Point(p.x - 1, p.y + 1))) {
            newP = new Point(p.x - 1, p.y);
          } else if (d == Dir.EAST &&
            !points.contains(new Point(p.x + 1, p.y - 1)) &&
            !points.contains(new Point(p.x + 1, p.y)) &&
            !points.contains(new Point(p.x + 1, p.y + 1))) {
            newP = new Point(p.x + 1, p.y);
          } else {
            continue;
          }

          // Keep track of repeated proposals
          proposals.put(p, newP);
          if (!proposalValues.contains(newP)) {
            proposalValues.add(newP);
          } else {
            repeated.add(newP);
          }
          break;
        }
      }

      if (proposals.isEmpty()) {
        break;
      }

      for (Map.Entry<Point, Point> e : proposals.entrySet()) {
        if (repeated.contains(e.getValue())) {
          // Don't move this elf.
          continue;
        }

        // Update elf's location and next position to consider.
        points.remove(e.getKey());
        points.add(e.getValue());
      }
      nextDir = nextDir.next();
      move++;
    }

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Point p : points) {
      minX = Math.min(minX, p.x);
      maxX = Math.max(maxX, p.x);
      minY = Math.min(minY, p.y);
      maxY = Math.max(maxY, p.y);
    }

    System.out.println((maxX - minX + 1) * (maxY - minY + 1) - points.size());
    System.out.println(move + 1);
  }
}
