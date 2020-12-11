import sys

seats = []
for line in sys.stdin:
    seats.append(list(line))

def adjacents(seats, r, c):
    adj = 0
    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            if (i, j) != (0, 0) and 0 <= r + i < len(seats) and 0 <= c + j < len(seats[r + i]) and seats[r+i][c+j] == '#':
                adj += 1
    return adj

def seen(seats, r, c):
    def seat_take_in_direction(seats, r, c, i, j):
        while 0 <= r+i < len(seats) and 0 <= c+j < len(seats[r]):
            r += i
            c += j
            if seats[r][c] == '#':
                return True
            if seats[r][c] == 'L':
                return False
        return False
    seen = 0
    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            if (i, j) != (0, 0):
                seen += seat_take_in_direction(seats, r, c, i, j)
    return seen

def simulate(seats, func, min_seen_to_change):
    change = True
    while change:
        change = False
        new_seats = [list(' ' * len(seats[r])) for r in range(len(seats))]
        for r in range(len(seats)):
            for c in range(len(seats[r])):
                count = func(seats, r, c)
                if seats[r][c] == 'L' and count == 0:
                    new_seats[r][c] = '#'
                    change = True
                elif seats[r][c] == '#' and count >= min_seen_to_change:
                    new_seats[r][c] = 'L'
                    change = True
                else:
                    new_seats[r][c] = seats[r][c]
        seats = new_seats
    return sum([row.count('#') for row in seats])

print(simulate(seats, adjacents, 4))
print(simulate(seats, seen, 5))
