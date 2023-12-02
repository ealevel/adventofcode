import java.util.*;

public class Day13 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int s = 0;
    List<PList> all = new ArrayList<>();
    for (int idx = 1; sc.hasNext(); idx++) {
      String left = sc.next();
      String right = sc.next();

      PList lList = parse(left, 0).list;
      PList rList = parse(right, 0).list;

//      System.out.println(lList);
//      System.out.println(rList);

      if (lList.compareTo(rList) < 0) {
        s += idx;
      }

      all.add(lList);
      all.add(rList);
    }
    // Part 1
    System.out.println(s);

    // Part 2
    PList div2 = new PList(List.of(new PList(List.of(new PInt(2)))));
    PList div6 = new PList(List.of(new PList(List.of(new PInt(6)))));
    all.add(div2);
    all.add(div6);

    Collections.sort(all);
    int div2Idx = all.indexOf(div2) + 1;
    int div6Idx = all.indexOf(div6) + 1;
    System.out.println(div2Idx * div6Idx);
  }

  private static Next parse(String signal, int pos) {
    if (signal.charAt(pos) == '[') {
      pos++;
      PList list = new PList(new ArrayList<>());
      while (pos < signal.length()) {
        if (signal.charAt(pos) == '[') {
          Next next = parse(signal, pos);
          list.data.add(next.list);
          pos = next.nextPos;
        } else if (signal.charAt(pos) == ']') {
          return new Next(list, pos + 1);
        } else if (Character.isDigit(signal.charAt(pos))) {
          // Elements for this list.
          int val = 0;
          while (Character.isDigit(signal.charAt(pos))) {
            val = (10 * val) + (signal.charAt(pos) - '0');
            pos++;
          }
          list.data.add(new PInt(val));
        } else {
          pos++;
        }
      }
    }
    throw new IllegalArgumentException("Invalid signal: " + signal);
  }

  private static class Next {
    PList list;
    int nextPos;

    Next(PList list, int nextPos) {
      this.list = list;
      this.nextPos = nextPos;
    }
  }

  private interface Data extends Comparable<Data> {}

  private static class PInt implements Data {
    int val;

    PInt(int val) {
      this.val = val;
    }

    public String toString() {
      return Integer.toString(val);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof PInt) {
        return this.val == ((PInt) obj).val;
      }
      return false;
    }

    public int compareTo(Data that) {
      if (that instanceof PInt) {
        return Integer.compare(this.val, ((PInt) that).val);
      } else {
        // Convert PInt to PList and compare.
        return new PList(List.of(this)).compareTo(that);
      }
    }
  }

  private static class PList implements Data {
    List<Data> data;

    PList(List<Data> data) {
      this.data = data;
    }

    public String toString() {
      return Arrays.deepToString(data.toArray());
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof PList) {
        return this.data.equals(((PList) obj).data);
      }
      return false;
    }

    public int compareTo(Data that) {
      if (that instanceof PInt) {
        return this.compareTo(new PList(List.of(that)));
      } else {
        PList thatList = (PList) that;
        for (int i = 0; i < Math.min(this.data.size(), thatList.data.size()); i++) {
          int c = this.data.get(i).compareTo(thatList.data.get(i));
          if (c != 0) {
            return c;
          }
        }
        // The two are PList.
        if (this.data.size() < thatList.data.size()) {
          return -1;
        } else if (this.data.size() > thatList.data.size()) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }
}
