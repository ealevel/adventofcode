import sys
from enum import Enum
from collections import namedtuple

sys.setrecursionlimit(10_000)

UPWARD = 0
DOWNWARD = 1
LEFTWARD = 2
RIGHTWARD = 3

Dir = namedtuple('Dir', ['dr', 'dc', 'nextdir'])
next = {
  LEFTWARD: Dir(0, -1, {
    '|': [DOWNWARD, UPWARD],
    '/': [DOWNWARD],
    '\\': [UPWARD],
  }),
  RIGHTWARD: Dir(0, 1, {
    '|': [DOWNWARD, UPWARD],
    '/': [UPWARD],
    '\\': [DOWNWARD],
  }),
  DOWNWARD: Dir(1, 0, {
    '-': [LEFTWARD, RIGHTWARD],
    '/': [LEFTWARD],
    '\\': [RIGHTWARD],
  }),
  UPWARD: Dir(-1, 0, {
    '-': [LEFTWARD, RIGHTWARD],
    '/': [RIGHTWARD],
    '\\': [LEFTWARD],
  }),
}

def solve(m, r, c, dir):
  def run(r, c, dir):
    if dir in seen.get((r,c), {}) or not (0 <= r < len(m)) or not (0 <= c < len(m[0])):
      return # already explored or out of matrix
    seen.setdefault((r, c), set())
    seen[(r, c)].add(dir)
    if m[r][c] not in next[dir].nextdir:
      # same direction
      r1 = r + next[dir].dr
      c1 = c + next[dir].dc
      run(r1, c1, dir)
    else:
      for ndir in next[dir].nextdir[m[r][c]]:
        r1 = r + next[ndir].dr
        c1 = c + next[ndir].dc
        run(r1, c1, ndir)
  # debug(m, energized)
  seen = {} # {(x,y): set(dirs)}
  run(r, c, dir)
  return len(seen)

def debug(m, seen):
  for (r,c) in seen:
    m[r][c] = '#'
  for row in m:
    print(''.join(row))

m = []
for row in sys.stdin:
  m.append(list(row.strip()))

print(solve(m, 0, 0, RIGHTWARD))

print(max(
  max(solve(m, r, 0, RIGHTWARD) for r in range(len(m))),
  max(solve(m, r, len(m[0])-1, LEFTWARD) for r in range(len(m))),
  max(solve(m, 0, c, DOWNWARD) for c in range(len(m[0]))),
  max(solve(m, len(m)-1, c, UPWARD) for c in range(len(m[0]))),
))
