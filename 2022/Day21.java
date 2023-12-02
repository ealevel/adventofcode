import java.util.*;
import java.util.regex.Pattern;

public class Day21 {
  private enum Op {
    ADD, SUBTRACT, MULTIPLY, DIVIDE;

    static Op fromSign(char sign) {
      switch (sign) {
        case '+': return ADD;
        case '-': return SUBTRACT;
        case '*': return MULTIPLY;
        case '/': return DIVIDE;
      }
      throw new IllegalArgumentException("What's %s?".formatted(sign));
    }

    long apply(long a, long b) {
      switch (this) {
        case ADD: return a + b;
        case SUBTRACT: return a - b;
        case MULTIPLY: return a * b;
        case DIVIDE: return a / b;
      }
      throw new IllegalArgumentException("What's %s?".formatted(this));
    }
  };

  private static class MonkeyOp {
    Op op;
    String a;
    String b;

    MonkeyOp(Op op, String a, String b) {
      this.op = op;
      this.a = a;
      this.b = b;
    }
  }

  private static Map<String, Long> monkeyToVal = new HashMap<>();
  private static Map<String, List<MonkeyOp>> monkeyToOp = new HashMap<>();
  private static Map<String, String> monkeyEq = new HashMap<>();
  private static List<String> visited = new ArrayList<>();

  private static long calc(String name) {
    visited.add(name);
    if (monkeyEq.containsKey(name) && !visited.contains(monkeyEq.get(name))) {
      return calc(monkeyEq.get(name));
    } else if (monkeyToVal.containsKey(name)) {
      return monkeyToVal.get(name);
    } else {
      for (MonkeyOp m : monkeyToOp.get(name)) {
        if (!visited.contains(m.a) && !visited.contains(m.b)) {
          long a = calc(m.a);
          long b = calc(m.b);
          long res = m.op.apply(a, b);
          monkeyToVal.put(name, res);
          return res;
        }
      }
    }
    throw new RuntimeException("Something bad happened");
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNext()) {
      String line = sc.nextLine();
      String[] nameAndJob = line.split(": ");
      if (nameAndJob[0].equals("humn")) {
        // Ignore, only applies for part 2
      } else if (nameAndJob[0].equals("root")) {
        // Only applies for part 2
        String[] ops = nameAndJob[1].split(" ");
        monkeyEq.put(ops[0], ops[2]);
        monkeyEq.put(ops[2], ops[0]);
      } else if (Pattern.matches("\\d+", nameAndJob[1])) {
        monkeyToVal.put(nameAndJob[0], Long.parseLong(nameAndJob[1]));
      } else {
        String[] ops = nameAndJob[1].split(" ");
        MonkeyOp m = new MonkeyOp(Op.fromSign(ops[1].charAt(0)), ops[0], ops[2]);

        monkeyToOp.putIfAbsent(nameAndJob[0], new ArrayList<>());
        monkeyToOp.putIfAbsent(m.a, new ArrayList<>());
        monkeyToOp.putIfAbsent(m.b, new ArrayList<>());

        monkeyToOp.get(nameAndJob[0]).add(m);
        // This only applies for part 2
        if (m.op == Op.ADD) {
          // m = a + b -> a = m - b
          monkeyToOp.get(m.a).add(new MonkeyOp(Op.SUBTRACT, nameAndJob[0], m.b));
          monkeyToOp.get(m.b).add(new MonkeyOp(Op.SUBTRACT, nameAndJob[0], m.a));
        } else if (m.op == Op.SUBTRACT) {
          // m = a - b -> a = m + b
          monkeyToOp.get(m.a).add(new MonkeyOp(Op.ADD, nameAndJob[0], m.b));
          monkeyToOp.get(m.b).add(new MonkeyOp(Op.SUBTRACT, m.a, nameAndJob[0]));
        } else if (m.op == Op.MULTIPLY) {
          // m = a * b -> a = m / b -> b = m / a
          monkeyToOp.get(m.a).add(new MonkeyOp(Op.DIVIDE, nameAndJob[0], m.b));
          monkeyToOp.get(m.b).add(new MonkeyOp(Op.DIVIDE, nameAndJob[0], m.a));
        } else if (m.op == Op.DIVIDE) {
          // m = a / b -> a = m * b -> b = a / m
          monkeyToOp.get(m.a).add(new MonkeyOp(Op.MULTIPLY, nameAndJob[0], m.b));
          monkeyToOp.get(m.b).add(new MonkeyOp(Op.DIVIDE, m.a, nameAndJob[0]));
        }
      }
    }
    // Part #1:
    // System.out.println(calc("root"));
    // Part #2:
    System.out.println(calc("humn"));
  }
}
