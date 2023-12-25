import re
import sys

# Return: valid_tickets, invalid_sum
def invalid_fields(rules, nearby_tickets):
    invalid_sum = 0
    valid_tickets = []
    for t in nearby_tickets:
        valid_t = True
        for v in t:
            valid = False
            for ranges in rules.values():
                if match(ranges, [v]):
                    valid = True
                    break
            if not valid:
                invalid_sum += v
                valid_t = False
        if valid_t:
            valid_tickets.append(t)
    return valid_tickets, invalid_sum

def get_rows(tickets):
    rows = {}
    for t in tickets:
        for i, val in enumerate(t):
            if i not in rows: rows[i] = []
            rows[i].append(val)
    return rows

def match(ranges, values):
    for v in values:
        if not any(lo <= v <= hi for lo, hi in ranges):
            return False
    return True

# Return: field -> row id
def discover(rules, rows):
    r = {} # field -> row ids
    for i, values in enumerate(rows.values()):
        for f, ranges in rules.items():
            if match(ranges, values):
                if f not in r: r[f] = []
                r[f].append(i)
    d = {}
    while r:
        # get i in r with one element
        one = [f for f, ranges in r.items() if len(ranges) == 1][0]
        # d[f] = i
        d[one] = r[one][0]
        # remove i from all r[*], remove empty
        for f, ranges in r.items():
            ranges.remove(d[one])
        r = {k: v for k, v in r.items() if v}
    return d

rules = {}
for line in sys.stdin:
    line = line.rstrip()
    if not line: break
    m = re.match('([a-z ]+): (\d+)-(\d+) or (\d+)-(\d+)', line)
    fields = m.group(1)
    val1_1 = int(m.group(2))
    val1_2 = int(m.group(3))
    val2_1 = int(m.group(4))
    val2_2 = int(m.group(5))
    rules[fields] = [(val1_1, val1_2), (val2_1, val2_2)]

sys.stdin.readline() # ignore
my_ticket = [int(num) for num in sys.stdin.readline().split(',')]

sys.stdin.readline() # ignore
sys.stdin.readline() # ignore

nearby_tickets = []
for line in sys.stdin:
    nearby_tickets.append([int(num) for num in line.rstrip().split(',')])

valid_tickets, invalid_sum = invalid_fields(rules, nearby_tickets)
print(invalid_sum)

rows = get_rows(valid_tickets + [my_ticket])
matches = discover(rules, rows)

v = 1
for field, i in matches.items():
    if field.startswith('departure'):
        v *= my_ticket[i]
print(v)
