import sys

LOW = 0
HIGH = 1

def simul(iter):
  # init states
  ff = {} # {mod: on|off}
  con = {} # {mod: {prev: low|high}}
  for mod in modules.keys():
    if types[mod] == '%':
      ff[mod] = False
    if types[mod] == '&':
      con[mod] = {}
  for mod in modules.keys():
    for dest in modules[mod]:
      if dest in con:
        con[dest][mod] = LOW
  # simulate
  rcvd = [0, 0]
  for _ in range(iter):
    q = [('broadcaster', LOW, None)]
    while len(q) > 0:
      mod, pulse, prev = q.pop(0)
      rcvd[pulse] += 1
      if mod not in modules:
        continue # drop it
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
