import sys
from enum import Enum
import math

sys.setrecursionlimit(20_000)

class Dir(Enum):
  LEFT = 1
  RIGHT = 2
  UP = 3
  DOWN = 4

def count(m, cleanm, r, c, prev):
  cleanm[r][c] = m[r][c]
  if m[r][c] == '|':
    if prev == Dir.UP:
      return 1 + count(m, cleanm, r+1, c, Dir.UP)
    else:
      return 1 + count(m, cleanm, r-1, c, Dir.DOWN)
  elif m[r][c] == '-':
    if prev == Dir.LEFT:
      return 1 + count(m, cleanm, r, c+1, Dir.LEFT)
    else:
      return 1 + count(m, cleanm, r, c-1, Dir.RIGHT)
  elif m[r][c] == 'L':
    if prev == Dir.UP:
      return 1 + count(m, cleanm, r, c+1, Dir.LEFT)
    else:
      return 1 + count(m, cleanm, r-1, c, Dir.DOWN)
  elif m[r][c] == 'J':
    if prev == Dir.UP:
      return 1 + count(m, cleanm, r, c-1, Dir.RIGHT)
    else:
      return 1 + count(m, cleanm, r-1, c, Dir.DOWN)
  elif m[r][c] == '7':
    if prev == Dir.LEFT:
      return 1 + count(m, cleanm, r+1, c, Dir.UP)
    else:
      return 1 + count(m, cleanm, r, c-1, Dir.RIGHT)
  elif m[r][c] == 'F':
    if prev == Dir.RIGHT:
      return 1 + count(m, cleanm, r+1, c, Dir.UP)
    else:
      return 1 + count(m, cleanm, r, c+1, Dir.LEFT)
  return 1

# Count walls per row and determine if inside or outside or loop.
def count_walls(m):
  inside = 0
  for r in range(len(m)):
    walls = 0
    for c in range(len(m[r])):
      if m[r][c] == 'L':
        c += 1
        while m[r][c] == '-':
          c += 1
        if m[r][c] == '7':
          walls += 1
      elif m[r][c] == 'F':
        c += 1
        while m[r][c] == '-':
          c += 1
        if m[r][c] == 'J':
          walls += 1
      elif m[r][c] == '|':
        walls += 1
      elif m[r][c] == '.' and walls % 2 != 0:
        inside += 1
  return inside

m = []
cleanm = [] # map without unused pipes
r, c = 0, 0
row = 0
for line in sys.stdin.readlines():
  m.append(line.strip())
  cleanm.append(['.'] * len(m[-1]))
  if 'S' in line:
    r = row
    c = line.index('S')
  row += 1

if r < len(m)-1 and m[r+1][c] in '|JL':
  res1 = count(m, cleanm, r+1, c, Dir.UP) / 2
elif r >= 0 and m[r-1][c] in '|7F':
  res1 = count(m, cleanm, r-1, c, Dir.DOWN) / 2
elif c < len(m[r])-1 and m[r][c+1] in '-7J':
  res1 = count(m, cleanm, r, c+1, Dir.LEFT) / 2
elif c >= 0 and m[r][c-1] in '-FL':
  res1 = count(m, cleanm, r, c-1, Dir.RIGHT) / 2
else:
  raise 'error'

cleanm[r][c] = '|'

print(int(res1))
print(count_walls(cleanm))
