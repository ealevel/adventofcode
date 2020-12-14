import re
import sys

def process_mask(mask):
    ones   = 0
    zeroes = (1 << 37) - 1
    floats = 0
    for i in range(36):
        if mask[i] == '1':
            ones   |= (1 << i)
        if mask[i] == '0':
            zeroes ^= (1 << i)
        if mask[i] == 'X':
            floats |= (1 << i)
    return ones, zeroes, floats

def apply_mask_to_value(ones, zeroes, value):
    return (value | ones) & zeroes

def all_masks(floats):
    def compute_masks(floats, maskset):
        if floats in maskset: return
        maskset.add(floats)
        for i in range(36):
            if floats & (1 << i) != 0:
                compute_masks(floats - (1 << i), maskset)
    maskset = set()
    compute_masks(floats, maskset)
    return maskset

ones, zeroes, floats = 0, 0, 0
mem1 = {}
mem2 = {}
p = re.compile('mem\[(\d+)\] = (\d+)')
for line in sys.stdin:
    if line.startswith('mask'):
        mask = line.rstrip('\n').split(' ')[2][::-1]
        ones, zeroes, floats = process_mask(mask)
    else:
        m = p.match(line)
        address = int(m.group(1))
        value   = int(m.group(2))
        mem1[address] = apply_mask_to_value(ones, zeroes, value)
        base_address = ((address | ones) & (~floats))
        for mask in all_masks(floats):
            mem2[base_address | mask] = value

print(sum(mem1.values()))
print(sum(mem2.values()))
