import sys

grid = []
symbols = set()
gears = {}
r = 0
for line in sys.stdin:
  line = line.strip()
  grid.append(line)
  for c in range(len(line)):
    if not line[c].isdigit() and line[c] != '.':
      symbols.add((r, c))
    if line[c] == '*':
      gears[(r, c)] = []
  r += 1

nums = []
for r in range(len(grid)):
  n = 0
  include = False
  toadd = set()
  for c in range(len(grid[r])):
    if grid[r][c].isdigit():
      n = 10*n + int(grid[r][c])
      if any(val in symbols for val in [(r-1, c), (r+1,c), (r-1,c-1), (r,c-1), (r+1,c-1), (r-1,c+1), (r,c+1), (r+1,c+1)]):
        include = True
      for g in [(r-1, c), (r+1,c), (r-1,c-1), (r,c-1), (r+1,c-1), (r-1,c+1), (r,c+1), (r+1,c+1)]:
        if g in gears:
          toadd.add(g)
    elif n > 0:
      if include:
        nums.append(n)
      for g in toadd:
        if g in gears:
          gears[g].append(n)
      n = 0
      include = False
      toadd = set()
  if n > 0:
    if include:
      nums.append(n)
    for g in toadd:
      if g in gears:
        gears[g].append(n)

print(sum(nums))

# print(gears)
s = 0
for g in gears.values():
  if len(g) == 2:
    s += g[0] * g[1]
print(s)
