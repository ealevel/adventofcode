import sys

u = 0
i = 0
for g in sys.stdin.read().split('\n\n'):
    qs = [q for q in g.split('\n') if q]
    u += len(set.union(*[set(q) for q in qs]))
    i += len(set.intersection(*[set(q) for q in qs]))

print(u)
print(i)
