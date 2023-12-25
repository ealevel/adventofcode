import sys

jolts = []
for line in sys.stdin:
    jolts.append(int(line))
jolts.sort()

diff = {1: 0, 2: 0, 3: 1}
output = 0
for jolt in jolts:
    diff[jolt - output] += 1
    output = jolt

def count(idx, jolts, target, mem):
    output = jolts[idx - 1] if idx > 0 else 0
    if idx == len(jolts):
        return 1 if output == target else 0
    if idx in mem:
        return mem[idx]
    c = 0
    for i, jolt in enumerate(jolts[idx:], idx):
        if jolt - output > 3:
            break
        c += count(i + 1, jolts, target, mem)
    mem[idx] = c
    return c

print(diff[1] * diff[3])
print(count(0, jolts, output, {}))
