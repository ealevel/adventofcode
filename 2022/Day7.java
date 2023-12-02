import java.util.*;

public class Day7 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Dir head = new Dir("/", null);
    Dir curr = head;
    while (sc.hasNextLine()) {
      String[] word = sc.nextLine().split(" ");
      if (word[0].equals("$")) {
        if (word[1].equals("cd")) {
          if (word[2].equals("/")) {
            curr = head;
            continue;
          } else if (word[2].equals("..")) {
            curr = curr.prev;
            continue;
          }
          if (!curr.dirs.containsKey(word[2])) {
            curr.addDir(new Dir(word[2], curr));
          }
          curr = curr.dirs.get(word[2]);
        } else if (word[1].equals("ls")) {
          // Do nothing.
        }
      } else if (word[0].equals("dir")) {
        // We're ls a dir and found a dir
        if (!curr.dirs.containsKey(word[1])) {
          curr.addDir(new Dir(word[1], curr));
        }
      } else {
        // We're ls a dir and found a file.
        curr.addFile(new File(word[1], Integer.parseInt(word[0])));
      }
    }

    int size = head.computeSize();
    int available = 70000000 - size;
    int need = 30000000 - available;

    List<Dir> allDirs = new LinkedList<>();
    Queue<Dir> q = new LinkedList<>();
    q.add(head);
    while (!q.isEmpty()) {
      Dir dir = q.poll();
      allDirs.add(dir);
      for (Dir d : dir.dirs.values()) {
        q.add(d);
      }
    }

    // Part 1
    int sizes = 0;
    for (Dir d : allDirs) {
      if (d.size <= 100000) {
        sizes += d.size;
      }
    }
    System.out.println(sizes);

    // Part 2
    Dir minDir = null;
    for (Dir d : allDirs) {
      if (d.size >= need && (minDir == null || d.size < minDir.size)) {
        minDir = d;
      }
    }
    System.out.println(minDir.size);
  }

  static class Dir {
    String name;
    Dir prev;
    Map<String, File> files;
    Map<String, Dir> dirs;
    int size;

    Dir(String name, Dir prev) {
      this.name = name;
      this.prev = prev;
      this.dirs = new HashMap<>();
      this.files = new HashMap<>();
    }

    void addFile(File file) {
      files.put(file.name, file);
    }

    void addDir(Dir dir) {
      dirs.put(dir.name, dir);
    }

    int computeSize() {
      int size = 0;
      for (File f : files.values()) {
        size += f.size;
      }
      for (Dir d : dirs.values()) {
        size += d.computeSize();
      }
      this.size = size;
      return size;
    }
  }

  static class File {
    String name;
    int size;

    File(String name, int size) {
      this.name = name;
      this.size = size;
    }
  }
}
