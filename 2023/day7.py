import sys
from collections import Counter
from functools import cmp_to_key

def type(hand, use_jokers):
  counter = Counter(list(hand))

  # part 2
  if use_jokers:
    jokers = counter.pop('J', 0)
    if len(counter) > 0:
      counter[counter.most_common(1)[0][0]] += jokers
    else:
      counter['A'] = 5

  values = sorted(counter.values(), reverse=True)
  if values == [5]:
    return 7 # five of a kind
  if values == [4,1]:
    return 6 # four of a kind
  if values == [3,2]:
    return 5 # full house
  if values == [3,1,1]:
    return 4 # four of a kind
  if values == [2,2,1]:
    return 3 # two pairs
  if values == [2,1,1,1]:
    return 2 # one pair
  if values == [1,1,1,1,1]:
    return 1 # highest card
  raise Exception("Failure parsing: {}, values={}".format(hand, counter))

def label(card, use_jokers):
  if use_jokers:
    return list("J23456789TQKA").index(card)
  else:
    return list("23456789TJQKA").index(card)

def comp(c1, c2, use_jokers):
  t1 = type(c1, use_jokers)
  t2 = type(c2, use_jokers)
  if t1 != t2:
    return t1 - t2
  for i in range(5):
    if c1[i] != c2[i]:
      return label(c1[i], use_jokers) - label(c2[i], use_jokers)

hands = []
for line in sys.stdin:
  hand, bid = line.strip().split()
  hands.append((hand, int(bid)))

hands1 = sorted(hands, key=cmp_to_key(lambda c1, c2: comp(c1[0], c2[0], False)))
print(sum((rank + 1) * bid for rank, (_, bid) in enumerate(hands1)))

hands2 = sorted(hands, key=cmp_to_key(lambda c1, c2: comp(c1[0], c2[0], True)))
print(sum((rank + 1) * bid for rank, (_, bid) in enumerate(hands2)))
