import sys

inst = sys.stdin.readline().strip()
n = len(inst)
sys.stdin.readline() # empty

mp = {}
for line in sys.stdin.readlines():
  pos, next = list(map(lambda l: l.strip(), line.split('=')))[0:2]
  left, right = next[1:-1].split(', ')[0:2]
  mp[pos] = (left, right)

c = 0
pos = 'AAA'
while pos != 'ZZZ':
  if inst[c % n] == 'L':
    pos = mp[pos][0]
  else:
    pos = mp[pos][1]
  c += 1
print(c)

# c = 0
# init = [p for p in mp if p[-1] == 'A']
# pos = [p for p in mp if p[-1] == 'A']
# while not all(p[-1] == 'Z' for p in pos):
#   # if any(p[-1] == 'Z' for p in pos):
#   # if init == pos:
#   # if pos[0] == 'AAA':
#   if sum(p[-1] == 'Z' for p in pos) >= 4:
#     print(c, pos)
#   if inst[c % n] == 'L':
#     pos = [mp[p][0] for p in pos]
#   else:
#     pos = [mp[p][1] for p in pos]
#   c += 1
# print(c)
