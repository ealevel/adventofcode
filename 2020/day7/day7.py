import re
import sys

up_g = {} # {bag: [outer bag]}
down_g = {} # {bag: [(count, inner bag)]}
for line in sys.stdin:
    f = re.split(' bags contain |, ', line.strip().rstrip('.'))
    outer = f[0]
    down_g[outer] = []
    for bag in f[1:]:
        if bag == 'no other bags':
            continue
        m = re.match('(\d+) ([\w ]+) bags?', bag)
        num = int(m.group(1))
        inner = m.group(2)
        outer_bags = up_g.get(inner, set())
        outer_bags.add(outer)
        up_g[inner] = outer_bags
        down_g[outer].append((num, inner))

def count_up(bag, checked):
    if bag in checked:
        return 0
    checked.add(bag)
    num = 1
    for outer in up_g.get(bag, set()):
        num += count_up(outer, checked)
    return num

def count_down(bag, checked):
    if bag in checked:
        return checked[bag]
    count = 1
    for num, inner in down_g.get(bag, {}):
        checked[inner] = count_down(inner, checked)
        count += num * checked[inner]
    return count

print(count_up('shiny gold', set()) - 1)
print(count_down('shiny gold', {}) - 1)
