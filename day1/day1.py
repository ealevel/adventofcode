import sys

def two_sum(exp, want, lo=0):
    left = lo
    right = len(exp) - 1
    while left < right:
        s = exp[left] + exp[right]
        if s > want:
            right -= 1
        elif s < want:
            left += 1
        else:
            return exp[left] * exp[right]
            break
    raise ValueError

exp = []
for line in sys.stdin:
    exp.append(int(line))
exp.sort()

print(two_sum(exp, 2020))

for i in range(len(exp)):
    try:
        r = two_sum(exp, 2020 - exp[i], i+1)
        print(exp[i] * r)
        break
    except:
        continue
