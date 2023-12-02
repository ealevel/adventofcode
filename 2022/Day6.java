import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day6 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String line = sc.next();
    Map<Character, Integer> last4 = new HashMap<>();
    int count = 1;
    for (int i = 0; i < line.length(); i++) {
      last4.put(line.charAt(i), last4.getOrDefault(line.charAt(i), 0) + 1);
      if (i >= 14) {
        if (last4.get(line.charAt(i - 14)) > 1) {
          last4.put(line.charAt(i - 14), last4.getOrDefault(line.charAt(i - 14), 0) - 1);
        } else {
          last4.remove(line.charAt(i - 14));
        }
      }
      if (last4.size() == 14) {
        break;
      }
      count++;
    }
    System.out.println(count);
  }
}
