import sys

def seq(nums): # -> (first_val, last_val)
  if all(x == 0 for x in nums):
    return (0, 0)
  next = []
  for i in range(1, len(nums)):
    next.append(nums[i] - nums[i - 1])
  f, l = seq(next)
  return (nums[0] - f, nums[-1] + l)

first, last = 0, 0
for line in sys.stdin:
  nums = [int(x) for x in line.split()]
  f, l = seq(nums)
  first += f
  last += l
print(last)
print(first)
