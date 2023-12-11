import sys

lines = [line.strip() for line in sys.stdin]

def solve(empty_line_size):
  g = []
  extra_r = [empty_line_size-1] * len(lines)
  extra_c = [empty_line_size-1] * len(lines[0])
  for r in range(len(lines)):
    for c in range(len(lines[r])):
      if lines[r][c] == '#':
        g.append((r, c))
        extra_r[r] = 0
        extra_c[c] = 0

  cumr = []
  for r in range(len(extra_r)):
    prev = cumr[-1] if len(cumr) > 0 else 0
    cumr.append(prev + 1 + extra_r[r])
  cumc = []
  for c in range(len(extra_c)):
    prev = cumc[-1] if len(cumc) > 0 else 0
    cumc.append(prev + 1 + extra_c[c])

  cumm = 0
  for i in range(len(g)):
    for j in range(i+1, len(g)):
      cumm += abs(cumr[g[i][0]] - cumr[g[j][0]]) + abs(cumc[g[i][1]] - cumc[g[j][1]])
  return cumm

print(solve(2))
print(solve(1_000_000))
