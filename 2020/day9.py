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
    lo = -1
    hi = -1
    s = 0
    while True:
        if s > n:
            s -= arr[lo] if lo >= 0 else 0
            lo += 1
        elif s < n:
            s += arr[hi] if hi >= 0 else 0
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
