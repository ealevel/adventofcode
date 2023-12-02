import java.util.*;

// 1 2 -3 3 -2 0 4 -> 1 2 -3 4 0 3 -2
// 7 2 -3 3 -2 0 4
// 13 2 -3 3 -2 0 4
// 1 2 -9 3 -2 0 4
// 1 2 -3 3 -14 0 4
public class Day20 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<Long> file = new ArrayList<>();
    List<Integer> mix = new ArrayList<>(); // These are indices only, 0..n-1
    while (sc.hasNext()) {
      long val = sc.nextInt() * 811589153L;
      file.add(val);
      mix.add(mix.size());
    }

    int n = file.size();
    for (int t = 0; t < 10; t++) {
      for (int i = 0; i < n; i++) {
        long toMove = file.get(i);
        int index = mix.indexOf(i);
        int newIndex = Math.floorMod(index + toMove, n - 1);
        mix.remove(index);
        mix.add(newIndex, i);
      }
    }

    int idx0 = mix.indexOf(file.indexOf(0L));
    long[] grove = new long[]{
      file.get(mix.get((idx0 + 1000) % n)),
      file.get(mix.get((idx0 + 2000) % n)),
      file.get(mix.get((idx0 + 3000) % n))};
    System.out.println(" Indices: " + Arrays.toString(mix.toArray()));
    System.out.println("     Mix:" + Arrays.toString(Arrays.stream(mix.toArray()).map(idx -> file.get((int) idx)).toArray()));
    System.out.println("   Grove: " + Arrays.toString(grove));
    System.out.println("Solution: " + Arrays.stream(grove).sum());
  }
}
