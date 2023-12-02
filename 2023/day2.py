import sys
from functools import reduce

# Max valid dice count for part 1
max_count = {
    "red": 12,
    "green": 13,
    "blue": 14
}

sum_pos = 0
sum_pow = 0
for line in sys.stdin:
    tok = line.split(':')
    id = int(tok[0].strip().split()[1])
    sets = tok[1].strip().split(';')
    # print(id, sets)
    possible = True
    pow = {
        "red": 0,
        "green": 0,
        "blue": 0
    }
    for set in sets:
        rols = set.strip().split(',')
        for rol in rols:
            val = rol.strip().split(' ')
            if int(val[0].strip()) > max_count[val[1].strip()]:
                # Part 1: if any count is greater than the valid max_count,
                # the game is not possible.
                possible = False
            # Part 2: get max count per color.
            pow[val[1].strip()] = max(int(val[0].strip()), pow[val[1].strip()])
    if possible:
        sum_pos += id
    # Power of the game is the product of the max of all colors seen.
    sum_pow += reduce(lambda x,y: x*y, pow.values())

print("Part 1:", sum_pos)
print("Part 2:", sum_pow)
