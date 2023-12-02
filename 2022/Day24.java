import java.sql.Array;
import java.util.*;

public class Day24 {
  private record Vertex(int x, int y, int steps) {}
  private static Map<Vertex, List<Vertex>> m;
  private static Set<Vertex> visited;

  private static void genGraph() {
    m = new HashMap<>();
    for (int x = 0; x < W; x++) {
      for (int y = -1; y <= H; y++) {
        if (y == -1 && x != 0) {
          continue;
        } else if (y == H && x != W-1) {
          continue;
        }
        for (int s = 0; s < LCM; s++) {
          Vertex v = new Vertex(x, y, s);
          if (isOk((s+1)%LCM, x-1, y)) {
            m.putIfAbsent(v, new ArrayList<>());
            m.get(v).add(new Vertex(x-1, y, (s+1)%LCM));
          }
          if (isOk((s+1)%LCM, x+1, y)) {
            m.putIfAbsent(v, new ArrayList<>());
            m.get(v).add(new Vertex(x+1, y, (s+1)%LCM));
          }
          if (isOk((s+1)%LCM, x, y-1)) {
            m.putIfAbsent(v, new ArrayList<>());
            m.get(v).add(new Vertex(x, y-1, (s+1)%LCM));
          }
          if (isOk((s+1)%LCM, x, y+1)) {
            m.putIfAbsent(v, new ArrayList<>());
            m.get(v).add(new Vertex(x, y+1, (s+1)%LCM));
          }
          if (isOk((s+1)%LCM, x, y)) {
            m.putIfAbsent(v, new ArrayList<>());
            m.get(v).add(new Vertex(x, y, (s+1)%LCM));
          }
        }
      }
    }
  }

  private record DistVertex(int dist, Vertex vertex) {}

  private static int dikstra(int initSteps, int x, int y, int x1, int y1) {
    Map<Vertex, Integer> dist = new HashMap<>();
    PriorityQueue<DistVertex> q = new PriorityQueue<>(new Comparator<DistVertex>() {
      public int compare(DistVertex v1, DistVertex v2) {
        return Integer.compare(dist.get(v1.vertex), dist.get(v2.vertex));
      }
    });

    Vertex initV = new Vertex(x, y, initSteps);
    q.add(new DistVertex(0, initV));
    dist.put(initV, 0);

    while (!q.isEmpty()) {
      DistVertex v = q.poll();
      if (!m.containsKey(v.vertex)) {
        // Dead end
        continue;
      }
      for (Vertex w : m.get(v.vertex)) {
        dist.putIfAbsent(w, Integer.MAX_VALUE);
        if (v.dist + 1 < dist.get(w)) {
          q.add(new DistVertex(v.dist + 1, w));
          dist.put(w, v.dist + 1);
        }
      }
    }

    int best = Integer.MAX_VALUE;
    for (Map.Entry<Vertex, Integer> e : dist.entrySet()) {
      if (e.getKey().x == x1 && e.getKey().y == y1) {
        best = Math.min(best, e.getValue());
      }
    }
    return best;
  }

  private static int H, W, LCM;
  private static char[][] M;
  private static int[][][] dp;

  private static boolean isOk(int step, int x, int y) {
    if (x == 0 && y == -1) {
      // Start point
      return true;
    }
    if (x == W-1 && y == H) {
      // Finish point
      return true;
    }
    if (x < 0 || x >= W || y < 0 || y >= H) {
      // Hit a wall
      return false;
    }
    for (int i = 0; i < W; i++) {
      if (M[y][i] == '>' && x == Math.floorMod(i+step, W)) {
        return false;
      } else if (M[y][i] == '<' && x == Math.floorMod(i-step, W)) {
        return false;
      }
    }
    for (int i = 0; i < H; i++) {
      if (M[i][x] == 'v' && y == Math.floorMod(i+step, H)) {
        return false;
      } else if (M[i][x] == '^' && y == Math.floorMod(i-step, H)) {
        return false;
      }
    }
    return true;
  }

//  // A better idea would be to create a graph (x,y,stepsW,stepsH)
//  private static Integer smth(int initState, int steps, int x, int y, int x1, int y1, int bestSoFar) {
//    if (x == x1 && y == y1) {
//      return steps;
//    }
//    if (steps > bestSoFar) {
//      // Don't even try it. There's something better out there.
//      return bestSoFar;
//    }
//    if (y >= 0 && y < H && x >= 0 && x < W && dp[x][y][steps] > 0) {
//      return dp[x][y][steps];
//    }
//
//    int best = Integer.MAX_VALUE;
//    int init = 0;//r.nextInt(5);
//    for (int rn = init; rn < init+5; rn++) {
//      switch (rn % 5) {
//        case 0:
//          if (isOk(initState + steps + 1, x + 1, y)) {
//            best = Math.min(best, smth(initState, steps + 1, x + 1, y, x1, y1, Math.min(best, bestSoFar)));
//          }
//          break;
//        case 1:
//          if (isOk(initState + steps + 1, x, y + 1)) {
//            best = Math.min(best, smth(initState, steps + 1, x, y + 1, x1, y1, Math.min(best, bestSoFar)));
//          }
//          break;
//        case 2:
//          if (isOk(initState + steps + 1, x, y)) {
//            best = Math.min(best, smth(initState, steps + 1, x, y, x1, y1, Math.min(best, bestSoFar)));
//          }
//          break;
//        case 3:
//          if (isOk(initState + steps + 1, x - 1, y)) {
//            best = Math.min(best, smth(initState, steps + 1, x - 1, y, x1, y1, Math.min(best, bestSoFar)));
//          }
//          break;
//        case 4:
//          if (isOk(initState + steps + 1, x, y - 1)) {
//            best = Math.min(best, smth(initState, steps + 1, x, y - 1, x1, y1, Math.min(best, bestSoFar)));
//          }
//          break;
//      }
//    }
//
//    if (y >= 0 && y < H && x >= 0 && x < W) {
//      dp[x][y][steps] = best;
//    }
//    return best;
//  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String line = sc.next();
    List<String> lines = new ArrayList<>();
    while (!(line = sc.next()).contains("##")) {
      lines.add(line.substring(1, line.length()-1));
    }
    H = lines.size();
    W = lines.get(0).length();
    LCM = 700;
    M = new char[H][];
    for (int y = 0; y < lines.size(); y++) {
      M[y] = lines.get(y).toCharArray();
    }

    genGraph();
    visited = new HashSet<>();
    int d0 = dikstra(0, 0, -1, W-1, H);
    System.out.println(d0);

    int d1 = dikstra(d0, W-1, H, 0, -1);
    System.out.println(d1);

    int d2 = dikstra(d0+d1, 0, -1, W-1, H);
    System.out.println(d2);
    System.out.println("%d %d %d, sum=%d".formatted(d0, d1, d2, d0+d1+d2));

//    dp = new int[W][H][10000];
//    int d0 = smth(0, 0, 0, -1, W-1, H, Integer.MAX_VALUE);
//    System.out.println(d0);
  }
}
