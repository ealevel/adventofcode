import java.util.Scanner;

public class Day14 {
  private static boolean drop(char[][] matrix) {
    int x = 500;
    int y = 0;
    if (matrix[y][x] == 'o') {
      // This is full
      return false;
    }
    while (true) {
      if (y == 199) {
        // This has fell through
        return false;
      } else if (matrix[y+1][x] == '.') {
        // Fall down
        y++;
      } else if (matrix[y+1][x-1] == '.') {
        // Fall left and down
        x--;
        y++;
      } else if (matrix[y+1][x+1] == '.') {
        // Fall right and down
        x++;
        y++;
      } else {
        // Stay
        matrix[y][x] = 'o';
        return true;
      }
    }
  }

  private static int MAXY = 200;
  private static int MAXX = 1000;

  public static void main(String[] args) {
    char[][] matrix = new char[MAXY][MAXX];
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[i][j] = '.';
      }
    }

    Scanner sc = new Scanner(System.in);
    int maxy = 0;
    while (sc.hasNext()) {
      String line = sc.nextLine();
      String[] lines = line.split(" -> ");

      for (int i = 1; i < lines.length; i++) {
        String[] coord0 = lines[i-1].split(",");
        int x0 = Integer.parseInt(coord0[0]);
        int y0 = Integer.parseInt(coord0[1]);

        String[] coord1 = lines[i].split(",");
        int x1 = Integer.parseInt(coord1[0]);
        int y1 = Integer.parseInt(coord1[1]);

        maxy = Math.max(maxy, Math.max(y0, y1));
        if (x0 == x1) {
          for (int y = Math.min(y0, y1); y <= Math.max(y0, y1); y++) {
            matrix[y][x0] = '#';
          }
        } else {
          for (int x = Math.min(x0, x1); x <= Math.max(x0, x1); x++) {
            matrix[y0][x] = '#';
          }
        }
      }
    }

    // This loop is for part 2 only
    for (int x = 0; x < MAXX; x++) {
      matrix[maxy+2][x] = '#';
    }

    int cnt = 0;
    while (drop(matrix)) {
      cnt++;
    }
    System.out.println(cnt);

//    for (int y = 0; y < 200; y++) {
//      System.out.println(new String(matrix[y]));
//    }
  }
}
