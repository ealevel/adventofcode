import sys
import math

line1 = sys.stdin.readline().strip().split(':')[1].strip()
line2 = sys.stdin.readline().strip().split(':')[1].strip()

time = [int(t) for t in line1.split()]
dist = [int(t) for t in line2.split()]

time2 = int(line1.replace(' ', ''))
dist2 = int(line2.replace(' ', ''))

# Solve s in: s*t - s^2 > d => s^2 - t*s + d < 0
# Return numbers between floor(highest) - ceil(lowest) + 1
# That is, solutions that will produce longer distance than d.
def count(t, d):
    lo = (t - (t**2 - 4*d) ** 0.5) / 2
    hi = (t + (t**2 - 4*d) ** 0.5) / 2
    return math.floor(hi) - math.ceil(lo) + 1

res = 1
for t in range(len(time)):
    res *= count(time[t], dist[t])
print(res)

print(count(time2, dist2))
