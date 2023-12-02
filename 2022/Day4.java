import java.util.Arrays;
import java.util.Scanner;

public class Day4 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int overlaps1 = 0;
    int overlaps2 = 0;
    while (sc.hasNext()) {
      String[] line = sc.next().split(",");
      String[] str1 = line[0].split("-");
      String[] str2 = line[1].split("-");

      int range1a = Integer.parseInt(str1[0]);
      int range1b = Integer.parseInt(str1[1]);
      int range2a = Integer.parseInt(str2[0]);
      int range2b = Integer.parseInt(str2[1]);

      if (range1a <= range2a && range2b <= range1b) {
        overlaps1++;
      } else if (range2a <= range1a && range1b <= range2b) {
        overlaps1++;
      }

      if (range1a <= range2a && range2a <= range1b) {
        overlaps2++;
      } else if (range1a <= range2b && range2b <= range1b) {
        overlaps2++;
      } else if (range2a <= range1b && range1b <= range2b) {
        overlaps2++;
      } else if (range2a <= range1b && range1b <= range2b) {
        overlaps2++;
      }
    }
    System.out.println(overlaps1);
    System.out.println(overlaps2);
  }
}
