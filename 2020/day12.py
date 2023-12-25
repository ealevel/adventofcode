import re
import sys

EAST, SOUTH, WEST, NORTH = 0, 90, 180, 270
to_dir = {'N': NORTH, 'S': SOUTH, 'E': EAST, 'W': WEST}

def adv(x, y, dir, val):
    if dir == EAST:
        return (x + val, y)
    if dir == WEST:
        return (x - val, y)
    if dir == NORTH:
        return (x, y + val)
    if dir == SOUTH:
        return (x, y - val)

def rotate(x, y, degrees):
    if degrees == 0:
        return (x, y)
    if degrees == 90:
        return (y, -x)
    if degrees == 180:
        return (-x, -y)
    if degrees == 270:
        return (-y, x)

actions = []
p = re.compile('(\w)(\d+)')
for line in sys.stdin:
    m = p.match(line)
    a = m.group(1)
    val = int(m.group(2))
    actions.append((a, val))

x, y = 0, 0
dir = EAST
for a, val in actions:
    if a in {'N', 'S', 'E', 'W'}:
        x, y = adv(x, y, to_dir[a], val)
    if a == 'F':
        x, y = adv(x, y, dir, val)
    if a == 'L':
        dir = (360 + dir - val) % 360
    if a == 'R':
        dir = (dir + val) % 360
print(abs(x) + abs(y))

x, y = 0, 0
wx, wy = 10, 1
for a, val in actions:
    if a in {'N', 'S', 'E', 'W'}:
        wx, wy = adv(wx, wy, to_dir[a], val)
    if a == 'F':
        x, y = (x + wx * val, y + wy * val)
    if a == 'L':
        wx, wy = rotate(wx, wy, (360 - val) % 360)
    if a == 'R':
        wx, wy = rotate(wx, wy, val % 360)
print(abs(x) + abs(y))
