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
        s = up_g.get(inner, set())
        s.add(outer)
        up_g[inner] = s
        down_g[outer].append((num, inner))

def count_up(inner, checked):
    if inner in checked:
        return 0
    checked.add(inner)
    num = 1
    for outer in up_g.get(inner, set()):
        num += count_up(outer, checked)
    return num

def count_down(outer, checked):
    if outer in checked:
        return checked[outer]
    count = 1
    for num, inner in down_g.get(outer, {}):
        checked[inner] = count_down(inner, checked)
        count += num * checked[inner]
    return count

print(count_up('shiny gold', set()) - 1)
print(count_down('shiny gold', {}) - 1)
