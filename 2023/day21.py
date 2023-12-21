from collections import deque
import numpy as np
import sys

def bfs(n, s, steps, inf_boundaries):
  q = deque([(s, s, steps)])
  seen = set()
  seen.add((s, s))
  while q:
    r, c, left = q.popleft()
    if left == 0:
      continue
    for d in [(0,-1), (0,1), (-1,0), (1,0)]:
      if is_valid(n, r+d[0], c+d[1], seen, inf_boundaries):
        q.append((r+d[0], c+d[1], left-1))
        seen.add((r+d[0], c+d[1]))
  return sum(1 for (r, c) in seen if (r+c)%2 == steps%2)

def is_valid(n, r, c, seen, inf_boundaries):
  if inf_boundaries:
    return (r,c) not in seen and m[r%n][c%n] != '#'
  else:
    return 0 <= r < n and 0 <= c < n \
      and (r,c) not in seen and m[r][c] != '#'

def debug(pos):
  print()
  for i, row in enumerate(m):
    for j, val in enumerate(row):
      if (i,j) in pos:
        print('O', end='')
      else:
        print(val, end='')
    print() # end of row

def solve_eq(points, x):
  coefficients = np.polyfit(*zip(*points), 2)
  result = np.polyval(coefficients, x)
  return round(result)

m = []
for id, line in enumerate(sys.stdin):
  m.append(line.strip())
n = len(m)
s = len(m) // 2

print(bfs(n, s, 64, False))

points = [(i, bfs(n, s, 65+131*i, True)) for i in range(3)]
print(solve_eq(points, 26_501_365 // n))
