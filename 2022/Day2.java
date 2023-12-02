import java.util.Scanner;

public class Day2 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int score = 0;
    int score2 = 0;
    while (sc.hasNext()) {
      String play1 = sc.next();
      String play2 = sc.next();

      if (play2.equals("X")) {
        score += 1;
      } else if (play2.equals("Y")) {
        score += 2;
      } else if (play2.equals("Z")) {
        score += 3;
      }
      if (play1.equals("A")) {
        score += 6;
      } else if (play1.equals("B")) {
        score += 3;
      } else if (play1.equals("C")) {
//        score += 1;
      }

      if (play2.equals("Z")) {
        score2 += 6;
        // play to win
        if (play1.equals("A")) {
          // play Y
          score2 += 2;
        } else if (play1.equals("B")) {
          // play Z
          score2 += 3;
        } else if (play1.equals("C")) {
          // play X
          score2 += 1;
        }
      } else if (play2.equals("Y")) {
        score2 += 3;
        // play same
        if (play1.equals("A")) {
          // play X
          score2 += 1;
        } else if (play1.equals("B")) {
          // play Y
          score2 += 2;
        } else if (play1.equals("C")) {
          // play Z
          score2 += 3;
        }
      } else if (play2.equals("X")) {
        // play to lose
        if (play1.equals("A")) {
          // play Z
          score2 += 3;
        } else if (play1.equals("B")) {
          // play X
          score2 += 1;
        } else if (play1.equals("C")) {
          // play Y
          score2 += 2;
        }
      }
    }

    System.out.println(score);
    System.out.println(score2);
  }
}
