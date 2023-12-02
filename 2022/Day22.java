import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Map<Integer, Integer> firstRow = new HashMap<>(); // first row for this col
    Map<Integer, Integer> lastRow = new HashMap<>();
    Map<Integer, Integer> firstCol = new HashMap<>(); // first col for this row
    Map<Integer, Integer> lastCol = new HashMap<>();
    List<String> lines = new ArrayList<>();
    String instructions = null;
    int lineWidth = 0;
    int realWidth = Integer.MAX_VALUE;
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      if (line.isEmpty()) {
        instructions = sc.nextLine();
        break;
      }
      lineWidth = Math.max(lineWidth, line.length());
      lines.add(line);
      realWidth = Math.min(realWidth, line.strip().length());

      int y = lines.size() - 1;
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) != ' ') {
          // Update first/last row for column x
          if (!firstRow.containsKey(x)) {
            firstRow.put(x, y);
          }
          lastRow.put(x, y);
          // Update first/last column for row y
          if (!firstCol.containsKey(y)) {
            firstCol.put(y, x);
          }
          lastCol.put(y, x);
        }
      }
    }

    Pattern pattern = Pattern.compile("(\\d+|[LR])");
    Matcher matcher = pattern.matcher(instructions);
    List<Instruction> insts = new ArrayList<>();
    while (matcher.find()) {
      String gr = matcher.group();
      if (gr.equals("L")) {
        insts.add(new Turn(false));
      } else if (gr.equals("R")) {
        insts.add(new Turn(true));
      } else {
        insts.add(new Steps(Integer.parseInt(gr)));
      }
    }

    int x = firstCol.get(0);
    int y = 0;
    Facing facing = Facing.RIGHT;
    for (Instruction inst : insts) {
      if (inst instanceof Turn) {
        if (((Turn) inst).clockwise) {
          facing = facing.clockwise();
        } else {
          facing = facing.counterClockwise();
        }
      } else if (inst instanceof Steps) {
        for (int i = 0; i < ((Steps) inst).steps; i++) {
          int nextX = x + facing.x;
          int nextY = y + facing.y;
          if (nextX < firstCol.get(y)) {
            nextX = lastCol.get(y);
          } else if (nextX > lastCol.get(y)) {
            nextX = firstCol.get(y);
          }
          if (nextY < firstRow.get(x)) {
            nextY = lastRow.get(x);
          } else if (nextY > lastRow.get(x)) {
            nextY = firstRow.get(x);
          }
          if (lines.get(nextY).charAt(nextX) == '#') {
            // We cannot move more in this direction, go to next instruction.
            break;
          }
          x = nextX;
          y = nextY;
        }
      }
    }

    // Part #1
    System.out.println("x=%d y=%d facing=%s".formatted(x, y, facing));
    System.out.println(1000 * (y + 1) + 4 * (x + 1) + facing.val);

    int W = 50;
    char[][][] faces = new char[6][W][W];
    for (int i = 0; i < W; i++) {
      faces[1][i%W] = lines.get(i).substring(W, 2*W).toCharArray();
      faces[2][i%W] = lines.get(i).substring(2*W, 3*W).toCharArray();
    }
    for (int i = W; i < 2*W; i++) {
      faces[0][i%W] = lines.get(i).substring(W, 2*W).toCharArray();
    }
    for (int i = 2*W; i < 3*W; i++) {
      faces[5][i%W] = lines.get(i).substring(0, W).toCharArray();
      faces[4][i%W] = lines.get(i).substring(W, 2*W).toCharArray();
    }
    for (int i = 3*W; i < 4*W; i++) {
      faces[3][i%W] = lines.get(i).substring(0, W).toCharArray();
    }

    x = 0;
    y = 0;
    int faceId = 1;
    facing = Facing.RIGHT;
    faces[faceId][y][x] = '>';
    int iter = 0;
    for (Instruction inst : insts) {
      if (iter++ % 100 == 0) {
        printall(W, faces);
      }
      if (inst instanceof Turn) {
        if (((Turn) inst).clockwise) {
          facing = facing.clockwise();
        } else {
          facing = facing.counterClockwise();
        }
      } else if (inst instanceof Steps) {
        for (int i = 0; i < ((Steps) inst).steps; i++) {
          int nextX, nextY, nextFaceId;
          Facing nextFacing;
          if (faceId == 1) {
            if (facing == Facing.RIGHT && x == W - 1) {
              nextX = 0;
              nextY = y;
              nextFacing = Facing.RIGHT;
              nextFaceId = 2;
            } else if (facing == Facing.LEFT && x == 0) {
              nextX = 0;
              nextY = W - 1 - y;
              nextFacing = Facing.RIGHT;
              nextFaceId = 5;
            } else if (facing == Facing.DOWN && y == W - 1) {
              nextX = x;
              nextY = 0;
              nextFacing = Facing.DOWN;
              nextFaceId = 0;
            } else if (facing == Facing.UP && y == 0) {
              nextX = 0;
              nextY = x;
              nextFacing = Facing.RIGHT;
              nextFaceId = 3;
            } else {
              nextX = x + facing.x;
              nextY = y + facing.y;
              nextFaceId = faceId;
              nextFacing = facing;
            }
          } else if (faceId == 2) {
            if (facing == Facing.RIGHT && x == W - 1) {
              nextX = W - 1;
              nextY = W - 1 - y;
              nextFacing = Facing.LEFT;
              nextFaceId = 4;
            } else if (facing == Facing.LEFT && x == 0) {
              nextX = W - 1;
              nextY = y;
              nextFacing = Facing.LEFT;
              nextFaceId = 1;
            } else if (facing == Facing.DOWN && y == W - 1) {
              nextX = W - 1;
              nextY = x;
              nextFacing = Facing.LEFT;
              nextFaceId = 0;
            } else if (facing == Facing.UP && y == 0) {
              nextX = x;
              nextY = W - 1;
              nextFacing = Facing.UP;
              nextFaceId = 3;
            } else {
              nextX = x + facing.x;
              nextY = y + facing.y;
              nextFaceId = faceId;
              nextFacing = facing;
            }
          } else if (faceId == 0) {
            if (facing == Facing.RIGHT && x == W - 1) {
              nextX = y;
              nextY = W - 1;
              nextFacing = Facing.UP;
              nextFaceId = 2;
            } else if (facing == Facing.LEFT && x == 0) {
              nextX = y;
              nextY = 0;
              nextFacing = Facing.DOWN;
              nextFaceId = 5;
            } else if (facing == Facing.DOWN && y == W - 1) {
              nextX = x;
              nextY = 0;
              nextFacing = Facing.DOWN;
              nextFaceId = 4;
            } else if (facing == Facing.UP && y == 0) {
              nextX = x;
              nextY = W - 1;
              nextFacing = Facing.UP;
              nextFaceId = 1;
            } else {
              nextX = x + facing.x;
              nextY = y + facing.y;
              nextFaceId = faceId;
              nextFacing = facing;
            }
          } else if (faceId == 4) {
            if (facing == Facing.RIGHT && x == W - 1) {
              nextX = W - 1;
              nextY = W - 1 - y;
              nextFacing = Facing.LEFT;
              nextFaceId = 2;
            } else if (facing == Facing.LEFT && x == 0) {
              nextX = W - 1;
              nextY = y;
              nextFacing = Facing.LEFT;
              nextFaceId = 5;
            } else if (facing == Facing.DOWN && y == W - 1) {
              nextX = W - 1;
              nextY = x;
              nextFacing = Facing.LEFT;
              nextFaceId = 3;
            } else if (facing == Facing.UP && y == 0) {
              nextX = x;
              nextY = W - 1;
              nextFacing = Facing.UP;
              nextFaceId = 0;
            } else {
              nextX = x + facing.x;
              nextY = y + facing.y;
              nextFaceId = faceId;
              nextFacing = facing;
            }
          } else if (faceId == 5) {
            if (facing == Facing.RIGHT && x == W - 1) {
              nextX = 0;
              nextY = y;
              nextFacing = Facing.RIGHT;
              nextFaceId = 4;
            } else if (facing == Facing.LEFT && x == 0) {
              nextX = 0;
              nextY = W - 1 - y;
              nextFacing = Facing.RIGHT;
              nextFaceId = 1;
            } else if (facing == Facing.DOWN && y == W - 1) {
              nextX = x;
              nextY = 0;
              nextFacing = Facing.DOWN;
              nextFaceId = 3;
            } else if (facing == Facing.UP && y == 0) {
              nextX = 0;
              nextY = x;
              nextFacing = Facing.RIGHT;
              nextFaceId = 0;
            } else {
              nextX = x + facing.x;
              nextY = y + facing.y;
              nextFaceId = faceId;
              nextFacing = facing;
            }
          } else if (faceId == 3) {
            if (facing == Facing.RIGHT && x == W - 1) {
              nextX = y;
              nextY = W - 1;
              nextFacing = Facing.UP;
              nextFaceId = 4;
            } else if (facing == Facing.LEFT && x == 0) {
              nextX = y;
              nextY = 0;
              nextFacing = Facing.DOWN;
              nextFaceId = 1;
            } else if (facing == Facing.DOWN && y == W - 1) {
              nextX = x;
              nextY = 0;
              nextFacing = Facing.DOWN;
              nextFaceId = 2;
            } else if (facing == Facing.UP && y == 0) {
              nextX = x;
              nextY = W - 1;
              nextFacing = Facing.UP;
              nextFaceId = 5;
            } else {
              nextX = x + facing.x;
              nextY = y + facing.y;
              nextFaceId = faceId;
              nextFacing = facing;
            }
          } else {
            throw new IllegalStateException();
          }
          if (faces[nextFaceId][nextY][nextX] == '#') {
            // We cannot move more in this direction, go to next instruction.
            break;
          }
          x = nextX;
          y = nextY;
          faceId = nextFaceId;
          facing = nextFacing;
          faces[faceId][y][x] = facing.str;
        }
      }
    }

    printall(W, faces);
    // x=10 y=46 facing=DOWN faceId=4
    // 1000*(100+46+1)+4*(50+10+1)+1 = 147245
    System.out.println("x=%d y=%d facing=%s faceId=%d".formatted(x, y, facing, faceId));
    System.out.println(1000 * (y + 1) + 4 * (x + 1) + facing.val);
  }

  private static void printall(int W, char[][][] faces) {
    String spaces = "                                                  ";
    for (int y0 = 0; y0 < W; y0++) {
      System.out.println(spaces + new String(faces[1][y0]) + new String(faces[2][y0]));
    }
    for (int y0 = 0; y0 < W; y0++) {
      System.out.println(spaces + new String(faces[0][y0]));
    }
    for (int y0 = 0; y0 < W; y0++) {
      System.out.println(new String(faces[5][y0]) + new String(faces[4][y0]));
    }
    for (int y0 = 0; y0 < W; y0++) {
      System.out.println(new String(faces[3][y0]));
    }
  }

  private interface Instruction {}
  private record Steps(int steps) implements Instruction {}
  private record Turn(boolean clockwise) implements Instruction {}

  private enum Facing {
    RIGHT(0,  1,  0, '>'),
     DOWN(1,  0,  1, 'v'),
     LEFT(2, -1,  0, '<'),
       UP(3,  0, -1, '^');

    private int val, x, y;
    private char str;

    Facing(int val, int x, int y, char str) {
      this.val = val;
      this.x = x;
      this.y = y;
      this.str = str;
    }

    Facing clockwise() {
      switch (this) {
        case RIGHT: return DOWN;
        case DOWN: return LEFT;
        case LEFT: return UP;
        case UP: return RIGHT;
      }
      throw new IllegalArgumentException("What's %s?".formatted(this));
    }

    Facing counterClockwise() {
      switch (this) {
        case RIGHT: return UP;
        case DOWN: return RIGHT;
        case LEFT: return DOWN;
        case UP: return LEFT;
      }
      throw new IllegalArgumentException("What's %s?".formatted(this));
    }
  }
}
