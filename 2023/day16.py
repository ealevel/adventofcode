import sys
from enum import Enum

sys.setrecursionlimit(10_000_000)

class Dir(Enum):
  UPWARD = 0
  DOWNWARD = 1
  LEFTWARD = 2
  RIGHTWARD = 3

# {dir: (xdelta, ydelta, cell: [next dirs])}
next = {
  Dir.LEFTWARD: (-1, 0, {
    '|': [Dir.DOWNWARD, Dir.UPWARD],
    '/': [Dir.DOWNWARD],
    '\\': [Dir.UPWARD],
  }),
  Dir.RIGHTWARD: (1, 0, {
    '|': [Dir.DOWNWARD, Dir.UPWARD],
    '/': [Dir.UPWARD],
    '\\': [Dir.DOWNWARD],
  }),
  Dir.DOWNWARD: (0, 1, {
    '-': [Dir.LEFTWARD, Dir.RIGHTWARD],
    '/': [Dir.LEFTWARD],
    '\\': [Dir.RIGHTWARD],
  }),
  Dir.UPWARD: (0, -1, {
    '-': [Dir.LEFTWARD, Dir.RIGHTWARD],
    '/': [Dir.RIGHTWARD],
    '\\': [Dir.LEFTWARD],
  }),
}

def solve(m, x, y, dir):
  def run(m, x, y, dir, seen):
    if dir in seen.get((x,y), {}) or x < 0 or x >= len(m) or y < 0 or y >= len(m):
      return # already explored
    seen.setdefault((x, y), set())
    seen[(x, y)].add(dir)
    if m[y][x] not in next[dir][2]:
      # same direction
      x1 = x + next[dir][0]
      y1 = y + next[dir][1]
      run(m, x1, y1, dir, seen)
    else:
      for ndir in next[dir][2][m[y][x]]:
        x1 = x + next[ndir][0]
        y1 = y + next[ndir][1]
        run(m, x1, y1, ndir, seen)
  # debug(m, energized)
  seen = {} # {(x,y): set(dirs)}
  run(m, x, y, dir, seen)
  return len(seen)

def debug(m, seen):
  for (x,y) in seen:
    m[y][x] = '#'
  for row in m:
    print(''.join(row))

m = []
for row in sys.stdin:
  m.append([c for c in row.strip()])

print(solve(m, 0, 0, Dir.RIGHTWARD))

print(max(
  max(solve(m, x, 0, Dir.DOWNWARD) for x in range(len(m[0]))),
  max(solve(m, x, len(m[0])-1, Dir.UPWARD) for x in range(len(m[0]))),
  max(solve(m, 0, y, Dir.RIGHTWARD) for y in range(len(m))),
  max(solve(m, len(m)-1, y, Dir.LEFTWARD) for y in range(len(m)))))
