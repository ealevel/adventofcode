import sys
import re
from collections import namedtuple
import copy

def do_segments_intersect(p1, q1, p2, q2):
  if p1[1] == q1[1] and p2[1] == q2[1]:
    # both horizonal
    return p1[1] == p2[1] and max(p1[0], q1[0]) >= min(p2[0], q2[0]) and max(p2[0], q2[0]) >= min(p1[0], q1[0])
  if p1[0] == q1[0] and p2[0] == q2[0]:
    # both vertical
    return p1[0] == p2[0] and max(p1[1], q1[1]) >= min(p2[1], q2[1]) and max(p2[1], q2[1]) >= min(p1[1], q1[1])
  if p1[0] == q1[0] and p2[1] == q2[1]:
    # Segment 1 is vertical
    return p1[0] >= min(p2[0], q2[0]) and p1[0] <= max(p2[0], q2[0]) and p2[1] >= min(p1[1], q1[1]) and p2[1] <= max(p1[1], q1[1])
  if p1[1] == q1[1] and p2[0] == q2[0]:
    # Segment 1 is horizontal
    return p1[1] >= min(p2[1], q2[1]) and p1[1] <= max(p2[1], q2[1]) and p2[0] >= min(p1[0], q1[0]) and p2[0] <= max(p1[0], q1[0])
  return False

def fall1(bricks, idx):
  if bricks[idx][0][2] <= 1:
    return False # can't go lower
  bricks[idx][0][2] -= 1
  bricks[idx][1][2] -= 1
  for b in bricks[:idx]:
    if b[0][2] <= bricks[idx][0][2] <= b[1][2]:
      if do_segments_intersect(bricks[idx][0], bricks[idx][1], b[0], b[1]):
        # revert block to original position
        bricks[idx][0][2] += 1
        bricks[idx][1][2] += 1
        return False
  return True

def fall(bricks):
  fallen = 0
  for idx in range(len(bricks)):
    fell = 0
    while fall1(bricks, idx):
      fell = 1
    fallen += fell
  return fallen

bricks = []
for id, line in enumerate(sys.stdin, start=1):
  x0,y0,z0,x1,y1,z1 = [int(n) for n in re.split(r'[~,]', line.strip())][:]
  p0 = [x0,y0,z0]
  p1 = [x1,y1,z1]
  if z0 < z1:
    bricks.append((p0, p1))
  else:
    bricks.append((p1, p0))

bricks = sorted(bricks, key=lambda b: min(b[0][2], b[1][2]))
fall(bricks)

desintegratable = 0
all_would_fall = 0
for idx in range(len(bricks)):
  copyb = copy.deepcopy(bricks)
  del copyb[idx]
  would_fall = fall(copyb)
  if would_fall == 0:
    desintegratable += 1
  else:
    all_would_fall += would_fall

print(desintegratable)
print(all_would_fall)
