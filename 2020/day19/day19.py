import re
import sys

# returns (did_match, matched_str)
def matches(rules, idx, message):
    char, subr = rules[idx]
    if char:
        return (True, 1) if message[0] == char else (False, 0)
    for s in subr:
        remaining = message
        match = True
        for r in s:
            did_match, matched_size = matches(rules, r, remaining)
            if not did_match:
                match = False
                break
            remaining = remaining[matched_size:]
        if match:
            return True, len(message) - len(remaining)
    return False, 0

rules = {} # {rule number: (char, set([rules]))}
for line in sys.stdin:
    line = line.rstrip()
    if not line:
        break
    m = re.match('(\d+): ([\w\d |"]+)', line)
    id = int(m.group(1))
    defi = m.group(2)

    # single char
    m = re.match('"(\w)"', defi)
    if m:
        rules[id] = (m.group(1), None)
        continue

    # list? of sub-rules
    subr = []
    for rule in defi.split(' | '):
        subr.append([int(next) for next in rule.split(' ')])
    rules[id] = (None, subr)

c = 0
for line in sys.stdin:
    r = line.rstrip()
    did_match, matched_size = matches(rules, 0, r)
    if did_match and matched_size == len(r):
        c += 1

print(c)
