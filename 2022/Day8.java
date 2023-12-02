import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day8 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<String> lines = new ArrayList<>();
    while (sc.hasNext()) {
      lines.add(sc.next());
    }

    int n = lines.size();
    int[][] matrix = new int[n][n];
    for (int row = 0; row < n; row++) {
      for (int col = 0; col < n; col++) {
        matrix[row][col] = Integer.parseInt(Character.toString(lines.get(row).charAt(col)));
      }
    }


    int[][] maxLeft = new int[n][n];
    int[][] maxRight = new int[n][n];
    int[][] maxTop = new int[n][n];
    int[][] maxBottom = new int[n][n];

    for (int row = 0; row < n; row++) {
      int prevMax = matrix[row][0];
      for (int col = 0; col < n; col++) {
        maxLeft[row][col] = Math.max(prevMax, matrix[row][col]);
        prevMax = maxLeft[row][col];
      }

      prevMax = matrix[row][n-1];
      for (int col = n - 1; col >= 0; col--) {
        maxRight[row][col] = Math.max(prevMax, matrix[row][col]);
        prevMax = maxRight[row][col];
      }
    }

    for (int col = 0; col < n; col++) {
      int prevMax = matrix[0][col];
      for (int row = 0; row < n; row++) {
        maxTop[row][col] = Math.max(prevMax, matrix[row][col]);
        prevMax = maxTop[row][col];
      }

      prevMax = matrix[n-1][col];
      for (int row = n - 1; row >= 0; row--) {
        maxBottom[row][col] = Math.max(prevMax, matrix[row][col]);
        prevMax = maxBottom[row][col];
      }
    }

    int visible = 4*(n-1);
    for (int row = 1; row < n - 1; row++) {
      for (int col = 1; col < n - 1; col++) {
        int val = matrix[row][col];
        if (val > maxLeft[row][col-1] || val > maxRight[row][col+1] || val > maxTop[row-1][col] || val > maxBottom[row+1][col]) {
          visible++;
        }
      }
    }

    System.out.println(visible);

    // Part 2
    int maxScore = 0;
    for (int i = 1; i < n - 1; i++) {
      for (int j = 1; j < n - 1; j++) {
        int score = 1;

        // left view
        int view = 0;
        for (int x = i - 1; x >= 0; x--) {
          view++;
          if (matrix[x][j] >= matrix[i][j]) {
            break;
          }
        }
        score *= view;

        // right view
        view = 0;
        for (int x = i + 1; x < n; x++) {
          view++;
          if (matrix[x][j] >= matrix[i][j]) {
            break;
          }
        }
        score *= view;

        // top view
        view = 0;
        for (int y = j - 1; y >= 0; y--) {
          view++;
          if (matrix[i][y] >= matrix[i][j]) {
            break;
          }
        }
        score *= view;

        // bottom view
        view = 0;
        for (int y = j + 1; y < n; y++) {
          view++;
          if (matrix[i][y] >= matrix[i][j]) {
            break;
          }
        }
        score *= view;

        maxScore = Math.max(maxScore, score);
      }
    }

    System.out.println(maxScore);
  }
}
