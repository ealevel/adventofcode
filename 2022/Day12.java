import java.util.*;

public class Day12 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<char[]> lines = new ArrayList<>();
    Point S = null, E = null;
    while (sc.hasNext()) {
      String line = sc.next();
      lines.add(line.toCharArray());
      int idx;
      if ((idx = line.indexOf('S')) >= 0) {
        S = new Point(lines.size() - 1, idx, 0);
        lines.get(lines.size() - 1)[idx] = 'a';
      }
      if ((idx = line.indexOf('E')) >= 0) {
        E = new Point(lines.size() - 1, idx, 0);
        lines.get(lines.size() - 1)[idx] = 'z';
      }
    }

    // BFS
    int minDist = -1;
    Queue<Point> q = new LinkedList<>();
    q.add(E);
    boolean[][] visited = new boolean[lines.size()][lines.get(0).length];
    visited[E.x][E.y] = true;
    Point to;
    while (!q.isEmpty()) {
      Point p = q.poll();
      if (lines.get(p.x)[p.y] == 'a') {
        // For part 1, this applies only if p == S
        minDist = p.dist;
        break;
      }

      if (p.canMoveTo(to = new Point(p.x-1, p.y, p.dist + 1), lines) && !visited[to.x][to.y]) {
        q.add(to);
        visited[to.x][to.y] = true;
      }
      if (p.canMoveTo(to = new Point(p.x+1, p.y, p.dist + 1), lines) && !visited[to.x][to.y]) {
        q.add(to);
        visited[to.x][to.y] = true;
      }
      if (p.canMoveTo(to = new Point(p.x, p.y-1, p.dist + 1), lines) && !visited[to.x][to.y]) {
        q.add(to);
        visited[to.x][to.y] = true;
      }
      if (p.canMoveTo(to = new Point(p.x, p.y+1, p.dist + 1), lines) && !visited[to.x][to.y]) {
        q.add(to);
        visited[to.x][to.y] = true;
      }
    }

    System.out.println(minDist);
  }

  static class Point {
    int x, y, dist;

    Point(int x, int y, int dist) {
      this.x = x;
      this.y = y;
      this.dist = dist;
    }

    boolean canMoveTo(Point to, List<char[]> lines) {
      return to.x >= 0 && to.x < lines.size() &&
        to.y >= 0 && to.y < lines.get(x).length &&
        lines.get(x)[y] - lines.get(to.x)[to.y] <= 1;
    }
  }
}
