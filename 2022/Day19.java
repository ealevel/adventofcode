import java.util.*;
import java.util.regex.MatchResult;

public class Day19 {
  record Minerals(int ore, int clay, int obsidian, int geode) {
    public Minerals add(Minerals that) {
      return new Minerals(ore + that.ore, clay + that.clay, obsidian + that.obsidian, geode + that.geode);
    }

    public Minerals minus(Minerals that) {
      return new Minerals(ore - that.ore, clay - that.clay, obsidian - that.obsidian, geode - that.geode);
    }

    public boolean canBuy(Minerals that) {
      return ore - that.ore >= 0 &&
        clay - that.clay >= 0 &&
        obsidian - that.obsidian >= 0 &&
        geode - that.geode >= 0;
    }

    @Override
    public String toString() {
      return "%d:%d:%d:%d".formatted(ore, clay, obsidian, geode);
    }
  }

  private static Minerals oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost;
  private static Map<String, Integer> dp;
  private static int maxGeodeSeen;

  // - Always build as much geode robots as possible.
  // - If all robots can be build (except geode), then not building is not an option.
  // TODO: what else?
  //   - Keep track of best score so far, if it's not reachable by given branch, give up early.
  //   - A score is not reachable if we cannot build enough geode robot in time to produce that much.
  private static int maxGeode(int minute, Minerals robots, Minerals minerals) {
    if (minute == 0) {
      // Time's up
//      System.out.println("maxGeodeSeen = max(%d, %d)".formatted(maxGeodeSeen, minerals.geode));
      maxGeodeSeen = Math.max(maxGeodeSeen, minerals.geode);
      return minerals.geode;
    }

    // Assume we can build 1 geodo/robot per minute left
    // PLUS extract 1 geode/minute for every geode robot we already have.
    // How much geode can they extract in the remaining time?
    // 1 min = 0 new robots
    // 2 min = 1 robot = extract 1
    // 3 min = 2 robots = extract 1 + 2
    // 4 min = 3 robots = extract 1 + 2 + 3
    // 5 min = 4 robots = extract 1 + 2 + 3 + 4 = min * (min-1) / 2
    int maxPossible = ((minute - 1) * (minute)) / 2 + robots.geode * minute;
    if (minerals.geode + maxPossible < maxGeodeSeen) {
//      System.out.println("Cannot build %d in %d minutes".formatted(maxPossible, minute));
      return 0;
    }

    String key = "%d-%s-%s".formatted(minute, robots, minerals);
    if (dp.containsKey(key)) {
      return dp.get(key);
    }

    int geode = 0;
    if (minerals.canBuy(geodeRobotCost)) {
      // Always build a geode robot If it can be built.
      geode = maxGeode(minute - 1,
        robots.add(new Minerals(0, 0, 0, 1)),
        minerals.minus(geodeRobotCost).add(robots));
    } else {
      boolean canBuildAll = minerals.canBuy(obsidianRobotCost) && minerals.canBuy(clayRobotCost) && minerals.canBuy(oreRobotCost);
      if (!canBuildAll) {
        // If we can build all robots, then we must build something
        geode = maxGeode(minute - 1, robots, minerals.add(robots));
      }
      if (minerals.canBuy(obsidianRobotCost)) {
        geode = Math.max(geode,
          maxGeode(minute - 1,
            robots.add(new Minerals(0, 0, 1, 0)),
            minerals.minus(obsidianRobotCost).add(robots)));
      }
      if (minerals.canBuy(clayRobotCost)) {
        geode = Math.max(geode,
          maxGeode(minute - 1,
            robots.add(new Minerals(0, 1, 0, 0)),
            minerals.minus(clayRobotCost).add(robots)));
      }
      if (minerals.canBuy(oreRobotCost)) {
        geode = Math.max(geode,
          maxGeode(minute - 1,
            robots.add(new Minerals(1, 0, 0, 0)),
            minerals.minus(oreRobotCost).add(robots)));
      }
    }

    dp.put(key, geode);
    return geode;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int quality = 0;
    while (sc.hasNextLine()) {
      sc.findInLine("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
      MatchResult result = sc.match();
      int id = Integer.parseInt(result.group(1));
      int ore4Ore = Integer.parseInt(result.group(2));
      int ore4clay = Integer.parseInt(result.group(3));
      int ore4obsidian = Integer.parseInt(result.group(4));
      int clay4obsidian = Integer.parseInt(result.group(5));
      int ore4geode = Integer.parseInt(result.group(6));
      int obsidian4geode = Integer.parseInt(result.group(7));
      sc.nextLine(); // Read remaining of line

      oreRobotCost = new Minerals(ore4Ore, 0, 0, 0);
      clayRobotCost = new Minerals(ore4clay, 0, 0, 0);
      obsidianRobotCost = new Minerals(ore4obsidian, clay4obsidian, 0, 0);
      geodeRobotCost = new Minerals(ore4geode, 0, obsidian4geode, 0);
      dp = new HashMap<>();
      maxGeodeSeen = 0;
      int geodes = maxGeode(32,
        new Minerals(1, 0, 0, 0),
        new Minerals(0, 0, 0, 0));
      System.out.println("#%d: geodes=%d".formatted(id, geodes));
      quality += id * geodes;
    }
    System.out.println(quality);
  }
}
