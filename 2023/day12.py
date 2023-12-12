import sys

def count(row, damaged, s_idx, d_idx, mem):
  while s_idx < len(row) and row[s_idx] == '.':
    s_idx += 1
  if s_idx >= len(row):
    # no more springs, only valid if matched all damaged
    return d_idx == len(damaged)
  if d_idx == len(damaged):
    # only valid (and 1) if there's no damaged in remaining springs
    return '#' not in row[s_idx:]
    
  if (s_idx, d_idx) in mem:
    return mem[(s_idx,d_idx)]

  cnt = 0
  if row[s_idx] == '?':
    if is_valid(row, s_idx, damaged[d_idx]):
      # next damaged[d_idx] can be damaged
      cnt += count(row, damaged, s_idx+damaged[d_idx]+1, d_idx+1, mem)
    # also count as if this spring is operational
    cnt += count(row, damaged, s_idx+1, d_idx, mem)
  if row[s_idx] == '#':
    if is_valid(row, s_idx, damaged[d_idx]):
      # next damaged[d_idx] can be damaged
      cnt += count(row, damaged, s_idx+damaged[d_idx]+1, d_idx+1, mem)
    else:
      return 0 # invalid
  mem[(s_idx,d_idx)] = cnt
  return cnt

def is_valid(row, s_idx, size):
  if s_idx + size > len(row):
    # not enough springs left
    return False
  for offset in range(size):
    # only accept springs that are damaged or unknown
    if row[s_idx+offset] == '.':
      return False
  # next spring needs to be unknown or operational
  return s_idx+size == len(row) or row[s_idx+size] == '.' or row[s_idx+size] == '?'

c1 = 0
c2 = 0
for line in sys.stdin:
  row, damaged = line.split()[:]
  damaged = [int(s) for s in damaged.split(',')]
  c1 += count(row, damaged, 0, 0, {})
  c2 += count('?'.join([row] * 5), damaged*5, 0, 0, {})
print(c1)
print(c2)
