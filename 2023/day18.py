import sys
import re
from collections import namedtuple

Dir = namedtuple('Dir', ['dr', 'dc'])
dir = {
  'U': Dir(-1, 0),
  'D': Dir( 1, 0),
  'L': Dir( 0,-1),
  'R': Dir( 0, 1),
}
hexToDir = list('RDLU')

def solve(m):
  r,c = 0,0
  area = 0
  for d, n in m:
    r1 = r + dir[d].dr * n
    c1 = c + dir[d].dc * n
    if d in 'RL':
      # sum if R, rest if L
      area += (c1-c) * r
    r,c = r1,c1
  diam = sum(n for _, n in m)
  return abs(area) + diam // 2 + 1

part1 = []
part2 = []
for line in sys.stdin:
  match = re.search(r'(\w) (\d+) \(#(\w+)(\d)\)', line.strip())
  d1 = match.group(1)
  n1 = int(match.group(2))
  part1.append((d1, n1))
  n2 = int(match.group(3), 16) # hex-to-int
  d2 = hexToDir[int(match.group(4))]
  part2.append((d2, n2))

print(solve(part1))
print(solve(part2))
