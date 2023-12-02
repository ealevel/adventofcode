import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day11 {
//  private static Monkey[] monkeys = new Monkey[]{
//    new Monkey(0, List.of(79, 98), (worry) -> worry * 19, worry -> worry % 23 == 0, 2, 3),
//    new Monkey(1, List.of(54, 65, 75, 74), (worry) -> worry + 6, worry -> worry % 19 == 0, 2, 0),
//    new Monkey(2, List.of(79, 60, 97), (worry) -> worry * worry, worry -> worry % 13 == 0, 1, 3),
//    new Monkey(3, List.of(74), (worry) -> worry + 3, worry -> worry % 17 == 0, 0, 1),
//  };

  private static Monkey[] monkeys = new Monkey[]{
    new Monkey(0, List.of(89L, 84L, 88L, 78L, 70L), (worry) -> worry * 5, worry -> worry % 7, 6, 7),
    new Monkey(1, List.of(76L, 62L, 61L, 54L, 69L, 60L, 85L), (worry) -> worry + 1, worry -> worry % 17, 0, 6),
    new Monkey(2, List.of(83L, 89L, 53L), (worry) -> worry + 8, worry -> worry % 11, 5, 3),
    new Monkey(3, List.of(95L, 94L, 85L, 57L), (worry) -> worry + 4, worry -> worry % 13, 0, 1),
    new Monkey(4, List.of(82L, 98L), (worry) -> worry + 7, worry -> worry % 19, 5, 2),
    new Monkey(5, List.of(69L), (worry) -> worry + 2, worry -> worry % 2, 1, 3),
    new Monkey(6, List.of(82L, 70L, 58L, 87L, 59L, 99L, 92L, 65L), (worry) -> worry * 11, worry -> worry % 5, 7, 4),
    new Monkey(7, List.of(91L, 53L, 96L, 98L, 68L, 82L), (worry) -> worry * worry, worry -> worry % 3, 4, 2),
  };

  public static void main(String[] args) {
    long[] inspections = new long[monkeys.length];
    for (int i = 0; i < 10000; i++) {
      for (Monkey m : monkeys) {
        while (!m.items.isEmpty()) {
          inspections[m.id]++;
          long item = m.items.poll();
          item = m.operation.run(item);
//          item /= 3;
          item %= 9699690; // 7*17*11*13*19*2*5*3
          if (m.test.run(item) == 0) {
            monkeys[m.passMonkey].items.add(item);
          } else {
            monkeys[m.failMonkey].items.add(item);
          }
        }
      }
    }
    Arrays.sort(inspections);
    System.out.println(Arrays.toString(inspections));
    System.out.println(inspections[inspections.length - 2] * inspections[inspections.length - 1]);
  }

  static class Monkey {
    int id;
    Queue<Long> items;
    OperFunc operation;
    OperFunc test;
    int passMonkey;
    int failMonkey;

    Monkey(int id, List<Long> items, OperFunc operation, OperFunc test, int passMonkey, int failMonkey) {
      this.id = id;
      this.items = new LinkedList<>(items);
      this.operation = operation;
      this.test = test;
      this.passMonkey = passMonkey;
      this.failMonkey = failMonkey;
    }
  }

  interface OperFunc {
    long run(long worry);
  }
}