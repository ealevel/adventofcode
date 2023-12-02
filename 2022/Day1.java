import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day1 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int cal = 0;
    int max = 0;
    PriorityQueue<Integer> elves = new PriorityQueue<>(new Comparator<Integer>() {
      public int compare(Integer x, Integer y) {
        return -Integer.compare(x, y);
      }
    });
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      if (line.isEmpty()) {
        max = Math.max(max, cal);
        elves.add(cal);
        cal = 0;
      } else {
        cal += Integer.parseInt(line);
      }
    }
    int top3 = 0;
    top3 += elves.poll();
    top3 += elves.poll();
    top3 += elves.poll();
    System.out.println(max);
    System.out.println(top3);
  }
}
