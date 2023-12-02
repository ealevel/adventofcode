from functools import reduce
import re
import sys

# Max valid dice count for part 1
max_allowed = {
    "red": 12,
    "green": 13,
    "blue": 14
}

sum_pos = 0
sum_pow = 0
for line in sys.stdin:
    tok = line.split(':')
    id = int(tok[0].strip().split()[1])
    cubes = re.split(r'[;,]', tok[1].strip())
    max_count = {
        "red": 0,
        "green": 0,
        "blue": 0
    }
    for cube in cubes:
        val = cube.strip().split()
        num = int(val[0].strip())
        color = val[1].strip()
        max_count[color] = max(num, max_count[color])
    # Part 1: allow only sets that don't exceed max_allowed[color]
    if all(max_count[color] <= max_allowed[color] for color in max_count):
        sum_pos += id
    # Power of the game is the product of the max of all colors seen.
    sum_pow += reduce(lambda x,y: x*y, max_count.values())

print("Part 1:", sum_pos)
print("Part 2:", sum_pow)
