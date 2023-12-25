import sys

ids = []
for line in sys.stdin:
    id = 0
    for i, c in enumerate(line.rstrip()[::-1]):
        if c == 'B' or c == 'R':
            id += (1 << i)
    ids.append(id)

n = len(ids) + 1
s = sum(ids)
lo = min(ids)
hi = max(ids)
expected = (lo + hi) * n / 2

print(hi)
print(int(expected - s))
