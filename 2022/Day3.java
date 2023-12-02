import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Day3 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int priorities = 0;
    Queue<String> groups = new LinkedList<>();
    while (sc.hasNext()) {
      String line = sc.next();
      groups.add(line);
      String sack1 = line.substring(0, line.length() / 2);
      String sack2 = line.substring(line.length() / 2);

      Set<Character> char1 = new HashSet<>();
      for (char ch : sack1.toCharArray()) {
        char1.add(ch);
      }

      Set<Character> intersect = new HashSet<>();
      for (char ch : sack2.toCharArray()) {
        if (char1.contains(ch)) {
          intersect.add(ch);
        }
      }

      for (char ch : intersect) {
        if (Character.isLowerCase(ch)) {
          priorities += ch - 'a' + 1;
        } else {
          priorities += ch - 'A' + 1 + 26;
        }
      }
    }
    System.out.println(priorities);

    priorities = 0;
    while (!groups.isEmpty()) {
      String a = groups.poll();
      String b = groups.poll();
      String c = groups.poll();
      Set<Character> charA = new HashSet<>();
      for (char ch : a.toCharArray()) {
        charA.add(ch);
      }
      Set<Character> charB = new HashSet<>();
      for (char ch : b.toCharArray()) {
        charB.add(ch);
      }
      Set<Character> charC = new HashSet<>();
      for (char ch : c.toCharArray()) {
        charC.add(ch);
      }
      charA.retainAll(charB);
      charA.retainAll(charC);

      for (char ch : charA) {
        if (Character.isLowerCase(ch)) {
          priorities += ch - 'a' + 1;
        } else {
          priorities += ch - 'A' + 1 + 26;
        }
      }
    }
    System.out.println(priorities);
  }
}
