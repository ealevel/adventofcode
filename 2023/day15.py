import sys
from functools import reduce

def hash(word):
  return reduce(lambda s, ch: ((s + ord(ch)) * 17) & 0xff, (ch for ch in word), 0)

input = sys.stdin.readline().strip().split(',')
print(sum(hash(w) for w in input))

boxes = [{} for _ in range(256)]
for word in input:
  if word[-1] == '-':
    label = word[:-1]
    bid = hash(label)
    if label in boxes[bid]:
      del boxes[bid][label]
  else:
    label, focal_len = word.split('=')[:]
    bid = hash(label)
    boxes[bid][label] = int(focal_len)

# this works since dict iterator returns elements in insertion order
print(sum((bid+1)*(lid+1)*box[label] for bid, box in enumerate(boxes) for lid, label in enumerate(box.keys())))
