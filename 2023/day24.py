import sys
import re
from collections import namedtuple
from fractions import Fraction
import z3

Point = namedtuple('Point', ['x', 'y', 'z'])

def intersect(p0, p1, q0, q1):
  mp = Fraction(p1.y - p0.y, p1.x - p0.x)
  mq = Fraction(q1.y - q0.y, q1.x - q0.x)
  bp = p0.y - mp * p0.x
  bq = q0.y - mq * q0.x
  if mp == mq: # lines are parallel
    assert bp != bq # guess they're never the same line
    return None
  # p0.y == q0.y
  # mp * p0.x + bp == mq * q0.x + bq, p0.x = q0.x
  # x = (bq - bp) / (mp - mq)
  x = Fraction(bq - bp, mp - mq)
  y = mp * x + bp
  assert y == mq * x + bq # should satisfy Q too
  return Point(x, y, None)

def intersect_rays(p, dirp, q, dirq):
  p1 = Point(p.x+dirp.x, p.y+dirp.y, p.z+dirp.z)
  q1 = Point(q.x+dirq.x, q.y+dirq.y, q.z+dirq.z)
  mid = intersect(p, p1, q, q1)
  if mid:
    # verify intersection point falls in trajectory
    valid_p = (mid.x >= p.x) == (dirp.x >= 0) and (mid.y >= p.y) == (dirp.y >= 0)
    valid_q = (mid.x >= q.x) == (dirq.x >= 0) and (mid.y >= q.y) == (dirq.y >= 0)
    if valid_p and valid_q:
      return mid
  return None

def collision():
  x, y, z = z3.Real('x'), z3.Real('y'), z3.Real('z')
  vx, vy, vz = z3.Real('vx'), z3.Real('vy'), z3.Real('vz')
  s = z3.Solver()
  for i, line in enumerate(lines):
    (lx, ly, lz), (vlx, vly, vlz) = line
    t = z3.Real(f't_{i}')
    s.add(x + vx * t == lx + vlx * t)
    s.add(y + vy * t == ly + vly * t)
    s.add(z + vz * t == lz + vlz * t)
  assert s.check() == z3.sat # satisfies
  m = s.model()
  return Point(m[x].as_long(), m[y].as_long(), m[z].as_long())

minval = 200_000_000_000_000
maxval = 400_000_000_000_000

lines = []
for line in sys.stdin:
  match = re.search(r'(-?\d+), (-?\d+), (-?\d+) @ (-?\d+), (-?\d+), (-?\d+)', line.strip())
  px, py, pz, vx, vy, vz = [int(match.group(i+1)) for i in range(6)][:]
  lines.append((Point(px, py, pz), Point(vx, vy, vz)))

cnt = 0
for i in range(len(lines)):
  for j in range(i+1, len(lines)):
    mid = intersect_rays(lines[i][0], lines[i][1], lines[j][0], lines[j][1])
    if mid and minval <= mid.x <= maxval and minval <= mid.y <= maxval:
      cnt += 1
print(cnt)

p = collision()
print(p.x + p.y + p.z)
