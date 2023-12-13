import sys

def split(m, diff_chars):
  n = len(m)
  for mid in range(1,n):
    sz = min(mid, n-mid)
    left = m[mid-sz:mid]
    right = m[mid:mid+sz][::-1] # reversed
    if sum(c1 != c2 for c1, c2 in zip(''.join(left), ''.join(right))) == diff_chars:
      return mid
  return 0 # no middle

def run(valley, diff_chars):
  row_mid = split(valley, diff_chars)
  col_mid = split([''.join(row) for row in zip(*valley)], diff_chars) # transposed
  return 100 * row_mid + col_mid

mirrors = [[]]
for line in sys.stdin:
  if len(line.strip()) > 0:
    mirrors[-1].append(line.strip())
    continue
  mirrors.append([])

print(sum(run(m, 0) for m in mirrors))
print(sum(run(m, 1) for m in mirrors))
