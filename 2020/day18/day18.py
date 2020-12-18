import sys

def evalLeftToRight(a):
    s = a[0]
    op = None
    for e in a[1:]:
        if e in ('+', '*'):
            op = e
        elif op == '+':
            s += e
        elif op == '*':
            s *= e
    return s

def evalAddOverMult(a):
    b = [a[0]]
    sum = False
    for e in a[1:]:
        if e == '+':
            sum = True
        elif sum:
            sum = False
            b[-1] += e
        else:
            b.append(e)
    return evalLeftToRight(b)

def rfind(a, e):
    for idx in range(len(a) - 1, -1, -1):
        if a[idx] == e:
            return idx
    return -1

s = 0
for line in sys.stdin:
    stack = []
    exp = line.rstrip().replace(' ', '')
    for ch in exp:
        if ch == '(':
            stack.append(ch)
        elif ch == ')':
            idx = rfind(stack, '(')
            # s1 = evalLeftToRight(stack[idx+1:]) # uncomment for part 1
            s1 = evalAddOverMult(stack[idx+1:])
            stack = stack[:idx]
            stack.append(s1)
        elif ch in ('+', '*'):
            stack.append(ch)
        else:
            stack.append(int(ch))
    # s += evalLeftToRight(stack) # uncomment for part 1
    s += evalAddOverMult(stack)

print(s)
