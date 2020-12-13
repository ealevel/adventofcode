import sys

time_ready = int(sys.stdin.readline())
buses = [(i, int(bus)) for i, bus in enumerate(sys.stdin.readline().split(',')) if bus != 'x']

min_t = float('inf')
min_bus = 0
for _, bus in buses:
    t = bus * ((time_ready + bus - 1) // bus)
    if t < min_t:
        min_t = t
        min_bus = bus

print((min_t - time_ready) * min_bus)

def chinese_remainder(n, a):
    from functools import reduce
    def mul_inv(a, b):
        b0 = b
        x0, x1 = 0, 1
        if b == 1: return 1
        while a > 1:
            q = a // b
            a, b = b, a % b
            x0, x1 = x1 - q * x0, x0
        if x1 < 0: x1 += b0
        return x1

    sum = 0
    prod = reduce(lambda a, b: a*b, n)
    for n_i, a_i in zip(n, a):
        p = prod // n_i
        sum += a_i * mul_inv(p, n_i) * p
    return sum % prod

a = [n - a for (a, n) in buses]
n = [n for (_, n) in buses]
print(chinese_remainder(n, a))
