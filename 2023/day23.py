import sys

sys.setrecursionlimit(10_000)

def dfs(r, c, go_downhill=False):
  if (r,c) == (len(m)-1, len(m[0])-2):
    return 0
  prev = m[r][c]
  m[r][c] = 'O'
  longest = -float('inf')
  for (dr, dc, slope, downhill) in [(-1,0,'v','^'), (1,0,'^','v'), (0,-1,'>','<'), (0,1,'<','>')]:
    if go_downhill:
      if m[r+dr][c+dc] == downhill:
        longest = max(longest, 1+dfs(r+dr, c+dc))
    else:
      if m[r+dr][c+dc] == slope:
        longest = max(longest, 1+dfs(r+dr, c+dc, True))
      elif m[r+dr][c+dc] == downhill or m[r+dr][c+dc] == '.':
        longest = max(longest, 1+dfs(r+dr, c+dc))
  m[r][c] = prev
  return longest

def compress():
  def explore(r,c,cost):
    if cost > 0 and (r,c) in v:
      yield (r,c,cost) # found one end
    else:
      m[r][c] = 'O' if cost > 0 else 'X'
      for (dr, dc) in [(-1,0), (1,0), (0,-1), (0,1)]:
        if 0<=r+dr<len(m) and 0<=c+dc<len(m[0]) and m[r+dr][c+dc] in '.X':
          yield from explore(r+dr, c+dc,cost+1)

  # get vertices that connect multiple paths, i.e. 3 or more paths, not tunnels.
  v = set()
  v.add((0,1))
  v.add((len(m)-1, len(m[0])-2))
  for r in range(1,len(m)-1):
    for c in range(1,len(m[0])-1):
      if m[r][c] == '.' and sum(m[r+dr][c+dc] == '.' for (dr, dc) in [(-1,0), (1,0), (0,-1), (0,1)]) >= 3:
        v.add((r, c))

  # compute map of (r,c) -> (r',c',cost)
  next = {}
  for (r,c) in v:
    next[(r,c)] = []
  for (r,c) in v:
    for r1,c1,cost in explore(r,c,0):
      next[(r,c)].append((r1,c1,cost))
      next[(r1,c1)].append((r,c,cost))
  return next

def debug():
  print()
  for line in m:
    print(''.join(line))

def dfs2(r, c):
  if (r,c) == (len(m)-1, len(m[0])-2):
    return 0 # reached goal
  if (r,c) in seen:
    return -float('inf') # can't go this way
  seen.add((r,c))
  longest = max(cost + dfs2(r1, c1) for r1,c1,cost in nextm[(r,c)])
  seen.remove((r,c))
  return longest

m = []
for line in sys.stdin:
  m.append(list(line.strip()))

# part 1
print(dfs(0, 1, 0))

# part 2
for r in range(len(m)):
  for c in range(len(m[0])):
    if m[r][c] in '<>^v':
      m[r][c] = '.' # remove slopes
nextm = compress()
seen = set()
print(dfs2(0, 1))
