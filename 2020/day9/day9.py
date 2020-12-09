import sys

PREAMBLE = 25

def valid(arr, n):
    arr = sorted(arr)
    lo = 0
    hi = len(arr) - 1
    while lo < hi:
        s = arr[lo] + arr[hi]
        if s > n:
            hi -= 1
        elif s < n:
            lo += 1
        else:
            return True
    return False

def encryption_weakness(arr, n):
    sums = [arr[0]]
    for i in range(1, len(arr)):
        sums.append(sums[-1] + arr[i])
    lo = 0
    hi = 1
    while True:
        s = sums[hi] - sums[lo - 1] if lo > 0 else sums[hi]
        if s > n:
            lo += 1
        elif s < n:
            hi += 1
        else:
            return min(arr[lo:hi+1]) + max(arr[lo:hi+1])

nums = []
for line in sys.stdin:
    nums.append(int(line))

arr = nums[:PREAMBLE]
for i in range(PREAMBLE, len(nums)):
    if not valid(arr, nums[i]):
        print(nums[i])
        print(encryption_weakness(nums[:i], nums[i]))
        break
    arr.pop(0)
    arr.append(nums[i])
