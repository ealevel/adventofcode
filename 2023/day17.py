import sys
import heapq
from collections import namedtuple

UP = 1
DOWN = 2
LEFT = 3
RIGHT = 4

Dir = namedtuple('Dir', ['dr', 'dc', 'sides'])
dirs = {
  UP:    Dir(-1,  0, [LEFT, RIGHT]),
  DOWN:  Dir( 1,  0, [LEFT, RIGHT]),
  LEFT:  Dir( 0, -1, [DOWN, UP]),
  RIGHT: Dir( 0,  1, [DOWN, UP]),
}

def search(m, min_steps, max_steps):
  rows = len(m)
  cols = len(m[0])
  seen = {}
  pq = [] # [state=(cost,r,c,dir,steps)]
  heapq.heappush(pq, (m[0][1],0,1,RIGHT,1))
  heapq.heappush(pq, (m[1][0],1,0,DOWN,1))
  while len(pq) > 0:
    cost,r,c,dir,steps = heapq.heappop(pq)
    if (r,c,dir,steps) in seen:
      continue
    seen[(r,c,dir,steps)] = True
    if r == rows-1 and c == cols-1 and steps >= min_steps:
      return cost
    if steps < max_steps:
      # can keep going straight
      r1 = r + dirs[dir].dr
      c1 = c + dirs[dir].dc
      if 0 <= r1 < rows and 0 <= c1 < cols:
        heapq.heappush(pq, (cost+m[r1][c1],r1,c1,dir,steps+1))
    if steps >= min_steps:
      # can turn to the sides
      for ndir in dirs[dir].sides:
        r1 = r + dirs[ndir].dr
        c1 = c + dirs[ndir].dc
        if 0 <= r1 < rows and 0 <= c1 < cols:
          heapq.heappush(pq, (cost+m[r1][c1],r1,c1,ndir,1))

m = []
for line in sys.stdin:
  m.append([int(c) for c in line.strip()])
print(search(m,1,3))
print(search(m,4,10))
