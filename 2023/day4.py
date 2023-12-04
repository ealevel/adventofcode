import sys

points = 0
copies = {}
for id, __line in enumerate(sys.stdin):
  copies[id] = copies.get(id, 0) + 1 # +original card
  line = __line.strip().split(':')[1].split('|')
  winning = set(int(v) for v in line[0].strip().split())
  numbers = set(int(v) for v in line[1].strip().split())
  count = [v in winning for v in numbers].count(True)
  if count > 0:
    points += 2 ** (count-1)
  for i in range(count):
    copies[id+i+1] = copies.get(id+i+1, 0) + copies[id]
print(points)
print(sum(copies.values()))
