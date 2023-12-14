import sys
import copy

def fall_north(platf):
  for col in range(len(platf[0])):
    nxt = 0
    for i in range(len(platf)):
      if platf[i][col] == '#':
        nxt = i+1
      elif platf[i][col] == 'O':
        platf[i][col] = '.'
        platf[nxt][col] = 'O' # O falls
        nxt += 1
  return platf

def rotate_clockwise(platf):
  rotated = [[0] * len(platf) for _ in range(len(platf[0]))]
  for i in range(len(platf)):
    for j in range(len(platf[0])):
      rotated[j][len(platf) - 1 - i] = platf[i][j]
  return rotated

def cycle(platf):
  for _ in range(4):
    platf = rotate_clockwise(fall_north(platf))
  return platf

def score(platf):
  return sum(i * row.count('O') for i, row in enumerate(reversed(platf), start=1))

def debug(platf):
  print()
  for row in platf:
    print(''.join(row))

platf = []
for line in sys.stdin:
  platf.append(list(line.strip()))

print(score(fall_north(platf)))

mem = {} # {platf: t}
index = {} # {t: platf}
target = 1_000_000_000
for t in range(target):
  platf = cycle(platf)
  key = str(platf)
  if key in mem:
    start = mem[key]
    period = t - start
    break
  mem[key] = t
  index[t] = copy.deepcopy(platf)

idx = (target-start)%period+start
print(score(index[idx-1]))
