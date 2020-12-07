import re
import sys

up_g = {} # {bag: [bags that can contain it]}
down_g = {} # {bag: [(count, inner bag)]}
for line in sys.stdin:
    f = re.split(' bags contain |, ', line.strip().rstrip('.'))
    outer = f[0]
    down_g[outer] = []
    for inner in f[1:]:
        if inner == 'no other bags':
            continue
        m = re.match('(\d+) ([\w ]+) bags?', inner)
        num = int(m.group(1))
        bag = m.group(2)
        s = up_g.get(bag, set())
        s.add(outer)
        up_g[bag] = s
        down_g[outer].append((num, bag))

def count_up(outer, checked):
    if outer in checked:
        return 0
    checked.add(outer)
    num = 1
    for inner in up_g.get(outer, set()):
        num += count_up(inner, checked)
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
