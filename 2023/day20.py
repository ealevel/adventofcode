import sys

LOW = 0
HIGH = 1

def init_ff():
  ff = {} # {mod: on|off}
  for mod in modules.keys():
    if types[mod] == '%':
      ff[mod] = False
  return ff

def init_con():
  con = {} # {mod: {prev: low|high}}
  for mod in modules.keys():
    for dest in modules[mod]:
      if types.get(dest, None) == '&':
        con.setdefault(dest, {})
        con[dest][mod] = LOW
  return con

def simul(iter):
  ff = init_ff()
  con = init_con()
  rcvd = [0, 0]
  for t in range(iter):
    q = [('broadcaster', LOW, None)]
    while len(q) > 0:
      mod, pulse, prev = q.pop(0)
      rcvd[pulse] += 1
      if mod == 'ls' and pulse == HIGH:
        # Log who sent a high pulse to ls. ls will send a low pulse to rx when all
        # these simultaneously send a high pulse to it. Then LCM of these iterations.
        print('module {} sent high pulse to rx at iter={}'.format(prev, t))
      if mod not in modules:
        continue # sink output, drop it
      if types[mod] == '%':
        if pulse == LOW:
          if ff[mod]:
            ff[mod] = False
            q.extend((dest, LOW, mod) for dest in modules[mod])
          else:
            ff[mod] = True
            q.extend((dest, HIGH, mod) for dest in modules[mod])
      elif types[mod] == '&':
        con[mod][prev] = pulse
        if all(mem == HIGH for mem in con[mod].values()):
          q.extend((dest, LOW, mod) for dest in modules[mod])
        else:
          q.extend((dest, HIGH, mod) for dest in modules[mod])
      else:
          q.extend((dest, pulse, prev) for dest in modules[mod])
  return rcvd[LOW] * rcvd[HIGH]

modules = {}
types = {}
for line in sys.stdin:
  mod, dest = line.strip().split(' -> ')[:]
  if mod[0] in list('%&'):
    types[mod[1:]] = mod[0]
    mod = mod[1:]
  else:
    types[mod] = None
  dest = dest.split(', ')
  modules[mod] = dest

print(simul(1000))
simul(10000)
