import sys

ops = []
for line in sys.stdin:
    op = line.split()
    ops.append((op[0], int(op[1])))

def run(acc, count, seen):
    if count in seen:
        return acc
    seen.add(count)
    op = ops[count]
    if op[0] == 'acc':
        return run(acc + op[1], count + 1, seen)
    elif op[0] == 'jmp':
        return run(acc, count + op[1], seen)
    else:
        return run(acc, count + 1, seen)

def fix(acc, count, changed, seen):
    if count == len(ops):
        return acc
    if count in seen:
        return None
    seen.add(count)
    op = ops[count]
    if op[0] == 'acc':
        return fix(acc + op[1], count + 1, changed, seen)
    moves = []
    if op[0] == 'jmp':
        moves.append(fix(acc, count + op[1], changed, seen))
        if not changed:
            moves.append(fix(acc, count + 1, True, seen))
    else:
        moves.append(fix(acc, count + 1, changed, seen))
        if not changed:
            moves.append(fix(acc, count + op[1], True, seen))
    return next((it for it in moves if it is not None), None)

print(run(0, 0, set()))
print(fix(0, 0, False, set()))
