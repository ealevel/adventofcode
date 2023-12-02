import java.util.*;

public class Day5 {
  /*  [T]     [D]         [L]
      [R]     [S] [G]     [P]         [H]
      [G]     [H] [W]     [R] [L]     [P]
      [W]     [G] [F] [H] [S] [M]     [L]
      [Q]     [V] [B] [J] [H] [N] [R] [N]
      [M] [R] [R] [P] [M] [T] [H] [Q] [C]
      [F] [F] [Z] [H] [S] [Z] [T] [D] [S]
      [P] [H] [P] [Q] [P] [M] [P] [F] [D]
       1   2   3   4   5   6   7   8   9  */
  public static void main(String[] args) {
    String[] init = new String[]{
      "PFMQWGRT",
      "HFR",
      "PZRVGHSD",
      "QHPBFWG",
      "PSMJH",
      "MZTHSRPL",
      "PTHNML",
      "FDQR",
      "DSCNLPH"
    };
    List<Stack<Character>> stacks = new LinkedList<>();
    for (String line : init) {
      Stack<Character> stack = new Stack<>();
      for (char ch : line.toCharArray()) {
        stack.add(ch);
      }
      stacks.add(stack);
    }

    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      String[] line = sc.nextLine().split(" ");
      int amt = Integer.parseInt(line[1]);
      int from = Integer.parseInt(line[3]) - 1;
      int to = Integer.parseInt(line[5]) - 1;

      // Part 1
//      while (amt > 0) {
//        stacks.get(to).add(stacks.get(from).pop());
//        amt--;
//      }

      // Part 2
      int sz = stacks.get(from).size();
      stacks.get(to).addAll(stacks.get(from).subList(sz - amt, sz));
      while (amt > 0) {
        stacks.get(from).pop();
        amt--;
      }
    }

    String resp = "";
    for (Stack<Character> s : stacks) {
      if (!s.isEmpty()) {
        resp += s.peek();
      }
    }
    System.out.println(resp);
  }
}
