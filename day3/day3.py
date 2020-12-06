import sys

rows = []
for line in sys.stdin:
    rows.append(line.rstrip('\n'))

def trees(rows, right, down):
    r = 0
    c = 0
    trees = 0
    while r < len(rows):
        row = rows[r]
        if row[c % len(row)] == '#':
            trees += 1
        c += right
        r += down
    return trees

print(trees(rows, 3, 1))

ans  = trees(rows, 1, 1)
ans *= trees(rows, 3, 1)
ans *= trees(rows, 5, 1)
ans *= trees(rows, 7, 1)
ans *= trees(rows, 1, 2)

print(ans)
