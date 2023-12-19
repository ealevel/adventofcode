import sys
import re
from collections import namedtuple

Func = namedtuple('Func', ['part', 'func', 'debug'])
Rule = namedtuple('Rule', ['part', 'func', 'goto', 'debug'])
Workflow = namedtuple('Workflow', ['rules', 'default'])

# apply input to wf[name] recursively until reaching A or R
def apply(input, name):
  if name == 'A': return True
  if name == 'R': return False
  for (part, f, goto, _) in wf[name].rules:
    if f(input[part]):
      return apply(input, goto)
  return apply(input, wf[name].default) # apply last

# count how many [x,m,a,s] combinations satisfy *all* these rules
def eval(funcs):
  final_rating = 1
  for p in list('xmas'):
    f_p = [f.func for f in funcs if f.part == p]
    final_rating *= sum(all(f(v) for f in f_p) for v in range(1, 4001))
  return final_rating

def dfs(name, funcs):
  if name == 'A': return eval(funcs)
  if name == 'R': return 0
  allval = 0
  for f in wf[name].rules:
    # run dfs for each rule; when moving to the next rule, add NOT to all prev rules.
    funcs.append(Func(f.part, f.func, f.debug))
    allval += dfs(f.goto, funcs)
    del funcs[-1]
    funcs.append(Func(f.part, lambda p, f=f.func: not f(p), "not {}".format(f.debug)))
  allval += dfs(wf[name].default, funcs) # last rule
  if len(wf[name].rules) > 0:
    del funcs[-len(wf[name].rules):]
  return allval

wf = {} # {name -> Workflow([rules], default)}
for line in sys.stdin:
  match = re.search(r'(\w+){(.*?)}', line.strip())
  if not match:
    break # empty line
  name = match.group(1)
  in_rules = match.group(2).split(',')
  
  rules = []
  for rule in in_rules[:-1]:
    func, goto = rule.split(':')[:]
    if '>' in func:
      part, val = func.split('>')[:]
      func = lambda x,val=int(val): x > val
    else:
      part, val = func.split('<')[:]
      func = lambda x,val=int(val): x < val
    rules.append(Rule(part, func, goto, rule))
  wf[name] = Workflow(rules, in_rules[-1])

input = []
for line in sys.stdin:
  match = re.search(r'{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}', line.strip())
  x=int(match.group(1))
  m=int(match.group(2))
  a=int(match.group(3))
  s=int(match.group(4))
  input.append({'x':x, 'm':m, 'a':a, 's':s})

print(sum(sum(parts.values()) for parts in input if apply(parts, 'in')))
print(dfs('in', []))
