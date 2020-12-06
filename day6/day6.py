import sys

uqs = [set()]
iqs = [set()]
new = False
for line in sys.stdin:
    if len(line.strip()) == 0:
        uqs.append(set())
        iqs.append(set())
        new = True
        continue
    q = line.strip()
    uqs[-1].update(q)
    if new:
        iqs[-1].update(q)
    else:
        iqs[-1].intersection_update(q)
    new = False

print(sum(len(q) for q in uqs))
print(sum(len(q) for q in iqs))
