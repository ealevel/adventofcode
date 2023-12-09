import sys
import math

inst = sys.stdin.readline().strip()
n = len(inst)
sys.stdin.readline() # empty

mp = {}
for line in sys.stdin.readlines():
  pos, next = list(map(lambda l: l.strip(), line.split('=')))[0:2]
  left, right = next[1:-1].split(', ')[0:2]
  mp[pos] = (left, right)

def solve(pos, is_end):
  c = 0
  while not is_end(pos):
    if inst[c % n] == 'L':
      pos = mp[pos][0]
    else:
      pos = mp[pos][1]
    c += 1
  return c

print(solve('AAA', lambda pos: pos == 'ZZZ'))

# Get LCM of all steps from **A to **Z since there are loops and:
# - given the chain: [**A -> startLoop] -> [... -> **Z] -> [**Z -> startLoop] exists
# - there's exactly one **Z for each **A start
# - len(**A -> startLoop) == len(**Z -> startLoop)
steps = [solve(pos, lambda pos: pos[-1] == 'Z') for pos in mp if pos[-1] == 'A']
print(steps, math.lcm(*steps))
