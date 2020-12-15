starting_numbers = [8,11,0,19,1,2]
max_turns = 30000000

turns = [] # turn: number
prev  = {} # number: second to last turn
for t, num in enumerate(starting_numbers):
    turns.append(num)
    if t > 0:
        last_num = turns[t - 1]
        prev[last_num] = t - 1

for t in range(len(turns), max_turns):
    last_num = turns[t - 1]
    if last_num not in prev:
        turns.append(0)
    else:
        turns.append(t - 1 - prev[last_num])
    prev[last_num] = t - 1

print(turns[-1])
