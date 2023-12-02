import java.util.List;
import java.util.Scanner;
import java.util.Stack;

// >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
public class Day17 {
  private static long MOVES = 1_000_000_000_000L;
  private static List<List<String>> ROCKS = List.of(
    List.of("..@@@@."),
    List.of("...@...", "..@@@..", "...@..."),
    List.of("..@@@..", "....@..", "....@.."),  // This is reversed, because we add them to the stack in reverse order
    List.of("..@....", "..@....", "..@....", "..@...."),
    List.of("..@@...", "..@@...")
  );

  private static boolean canMoveDown(Stack<String> s, int heightTop) {
    for (int h = heightTop; h >= 0 && s.get(h).contains("@"); h--) {
      if (h == 0) {
        // This is the bottom
        return false;
      }
      String line = s.get(h);
      String below = s.get(h-1);
      for (int idx = line.indexOf('@'); idx < line.length(); idx++) {
        if (line.charAt(idx) == '@' && below.charAt(idx) == '#') {
          return false;
        }
      }
    }
    return true;
  }

  private static void moveDown(Stack<String> s, int heightTop) {
    int heightInit = heightTop;
    while (heightInit > 0 && s.get(heightInit-1).contains("@")) {
      heightInit--;
    }

    for (int h = heightInit; h <= heightTop; h++) {
      char[] line = s.get(h).toCharArray();
      char[] below = s.get(h-1).toCharArray();

      for (int idx = 0; idx < line.length; idx++) {
        if (line[idx] == '@') {
          below[idx] = '@';
          line[idx] = '.';
        }
      }

      s.set(h, new String(line));
      s.set(h-1, new String(below));
    }
  }

  private static void stop(Stack<String> s, int heightTop) {
    for (int h = heightTop; h >= 0 && s.get(h).contains("@"); h--) {
      s.set(h, s.get(h).replace('@', '#'));
    }
  }

  private static void moveRight(Stack<String> s, int heightTop) {
    for (int h = heightTop; h >= 0 && s.get(h).contains("@"); h--) {
      char[] line = s.get(h).toCharArray();
      for (int idx = 0; idx < line.length; idx++) {
        if (line[idx] == '@' && (idx == line.length-1 || line[idx+1] == '#')) {
          // Cannot move - it's either blocked or we hit a wall
          return;
        }
      }
    }

    // Move
    for (int h = heightTop; h >= 0 && s.get(h).contains("@"); h--) {
      char[] line = s.get(h).toCharArray();
      for (int idx = line.length-1; idx > 0; idx--) {
        if (line[idx-1] == '@') {
          line[idx] = '@';
          line[idx-1] = '.';
        }
      }
      s.set(h, new String(line));
    }
  }

  private static void moveLeft(Stack<String> s, int heightTop) {
    for (int h = heightTop; h >= 0 && s.get(h).contains("@"); h--) {
      char[] line = s.get(h).toCharArray();
      for (int idx = 0; idx < line.length; idx++) {
        if (line[idx] == '@' && (idx == 0 || line[idx-1] == '#')) {
          // Cannot move - it's either blocked or we hit a wall
          return;
        }
      }
    }

    // Move
    for (int h = heightTop; h >= 0 && s.get(h).contains("@"); h--) {
      char[] line = s.get(h).toCharArray();
      for (int idx = 0; idx < line.length-1; idx++) {
        if (line[idx+1] == '@') {
          line[idx] = '@';
          line[idx+1] = '.';
        }
      }
      s.set(h, new String(line));
    }
  }

  private static int addAndMove(char[] gases, Stack<String> s, List<String> rock, int nextGas) {
    for (int i = 0; i < 3; i++) {
      s.add(".......");
    }
    s.addAll(rock);

    int height = s.size()-1; // Top of the falling rock
    while (true) {
      // Gas blows, move if possible and update next gas position
      if (gases[nextGas] == '<') {
        moveLeft(s, height);
      } else {
        moveRight(s, height);
      }
      nextGas = (nextGas + 1) % gases.length;

      // If rock cannot move down, stop
      if (!canMoveDown(s, height)) {
        stop(s, height);
        break;
      }

      // Move down and iterate
      moveDown(s, height);
      height--;
    }

    // Remove all empty spaces in the top
    while (!s.peek().contains("#")) {
      s.pop();
    }

    return nextGas;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    char[] gases = sc.next().toCharArray();
    int nextRock = 0, nextGas = 0;
    Stack<String> s = new Stack<>();

    String key = "";
    String repeat = "";
    long idx = 0;
    long first_iter = 0;
    long first_height = 0;
    long freq = 0;
    long freq_height_diff = 0;
    for (idx = 0; idx < MOVES; idx++) {
      nextGas = addAndMove(gases, s, ROCKS.get(nextRock), nextGas);
      nextRock = (nextRock + 1) % ROCKS.size();

      // I manually looked for a pattern that repeated but would that block all rocks from falling through, hence
      // limiting the potential of rocks always falling through some cracks and not showing the correct repetition.
      // This could be done automatically too by checking the top N rows and verifying it's blocked and then use such
      // pattern.
      //
      // For part 1:
      //      if (s.size() > 5 && s.get(s.size()-3).equals("###.###") && s.get(s.size()-4).equals(".#####.")) {
      //        key = "|%s|%s|%s|%s|%d:%d".formatted(s.get(s.size()-1), s.get(s.size()-2), s.get(s.size()-3), s.get(s.size()-4), nextRock, nextGas);
      // For part 2:
      if (s.size() > 2 && s.get(s.size()-2).equals("#######")) {
        key = "|%s|%s|%d:%d".formatted(s.get(s.size()-1), s.get(s.size()-2), nextRock, nextGas);
        if (first_iter == 0) {
          // First time we find this
          repeat = key;
          first_iter = idx;
          first_height = s.size();
        } else if (key.equals(repeat)) {
          // Second time, record frequency and break
          freq = idx - first_iter;
          freq_height_diff = s.size() - first_height;
          break;
        }
      }
    }

    long x = 0;
    if (idx < MOVES) {
      x = (MOVES - idx) / freq; // how many more iterations of freq we can do before reaching MOVES
      for (long i = idx + freq * x + 1; i < MOVES; i++) {
        nextGas = addAndMove(gases, s, ROCKS.get(nextRock), nextGas);
        nextRock = (nextRock + 1) % ROCKS.size();
      }
    }

    System.out.println(s.size() + freq_height_diff * x);
  }
}
