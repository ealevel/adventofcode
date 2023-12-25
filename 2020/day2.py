import sys
import re

tokens = []
for line in sys.stdin:
    tkn = re.split(r'-| |: ', line.rstrip('\n'))
    tokens.append(tkn)

valid1 = 0
valid2 = 0
for tkn in tokens:
    left   = int(tkn[0])
    right  = int(tkn[1])
    ch     = tkn[2]
    passwd = tkn[3]
    c = passwd.count(ch)
    if left <= c and c <= right:
        valid1 += 1
    if (passwd[left - 1] == ch) ^ (passwd[right - 1] == ch):
        valid2 += 1

print(valid1)
print(valid2)
