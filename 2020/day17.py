import sys

cycles = 6

def neighbors(s, x, y, z, w):
    adj = []
    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            for k in [-1, 0, 1]:
                for v in [-1, 0, 1]:
                    if (i, j, k, v) != (0, 0, 0, 0) and s.get((x + i, y + j, z + k, w + v), False):
                        adj.append((x + i, y + j, z + k, w + v))
    return adj

s = {} # {[x,y,z,w]: is_active?}
sz = 0
for x, line in enumerate(sys.stdin):
    sz += 1
    for y, ch in enumerate(line):
        s[(x, y, 0, 0)] = ch == '#'

for iter in range(1, cycles + 1):
    t = {}
    for x in range(-iter, sz + iter):
        for y in range(-iter, sz + iter):
            for z in range(-iter, sz + iter):
                # for w in range(1): # uncomment for part 1
                for w in range(-iter, sz + iter):
                    adj = neighbors(s, x, y, z, w)
                    if s.get((x, y, z, w), False) and 2 <= len(adj) <= 3:
                        t[(x, y, z, w)] = True
                    elif not s.get((x, y, z, w), False) and len(adj) == 3:
                        t[(x, y, z, w)] = True
                    else:
                        t[(x, y, z, w)] = False
    s = t

print(sum(1 for v in s.values() if v))
