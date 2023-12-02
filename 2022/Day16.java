import java.util.*;
import java.util.regex.MatchResult;

public class Day16 {
  private static int EXTRA_PLAYERS = 1;
  private static int STEPS = 26;
  private static Map<String, Integer> rates = new HashMap<>();
  private static Map<String, String[]> graph = new HashMap<>();
  private static Map<String, Integer> index = new HashMap<>();
  private static Map<String, Integer> dp = new HashMap<>();

  private static int maxPressure(int min, String pos, long bs, int player) {
    if (min == 0) {
      // Time's up
      if (player > 0) {
        // If there's another player, run it with existing bitset
        return maxPressure(STEPS, "AA", bs, player-1);
      }
      return 0;
    }

    String key = "%d:%d:%d".formatted(min + STEPS * EXTRA_PLAYERS, pos, bs);
    if (dp.containsKey(key)) {
      return dp.get(key);
    }

    int max = 0;
    if (rates.get(pos) > 0 && (bs & (1L << index.get(pos))) == 0) {
      // Open valve
      max = Math.max(max, rates.get(pos) * (min - 1) + maxPressure(min-1, pos, bs | (1L << index.get(pos)), player));
    }

    for (String to : graph.get(pos)) {
      // Just move to next valve
      max = Math.max(max, maxPressure(min-1, to, bs, player));
    }

    dp.put(key, max);
    return max;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int idx = 0;
    while (sc.hasNextLine()) {
      sc.findInLine("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? ([\\w, ]+)");
      MatchResult result = sc.match();
      String from = result.group(1);
      int rate = Integer.parseInt(result.group(2));
      String[] to = result.group(3).split(", ");
      sc.nextLine(); // Read remaining of line

      rates.put(from, rate);
      graph.put(from, to);
      index.put(from, idx);
      idx++;
    }

    System.out.println(maxPressure(STEPS, "AA", 0, EXTRA_PLAYERS));
  }
}
