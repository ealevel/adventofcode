import sys
import queue

def bfs(source, ignored_edges=[]):
  q = queue.Queue()
  visited = set()
  q.put((source, 1))
  visited.add(source)
  prev = {}
  while not q.empty():
    node, cost = q.get()
    for next in m[node]:
      if next not in visited \
          and (node, next) not in ignored_edges \
          and (next, node) not in ignored_edges:
        q.put((next, cost+1))
        visited.add(next)
        prev[next] = node
  return prev, len(visited)

def to_edges(source, sink, prev_map):
  edges = []
  node = sink
  while node != source:
    prev = prev_map[node]
    edges.append((prev, node))
    node = prev
  return edges

m = {}
for line in sys.stdin:
  node, allto = line.strip().split(': ')[:]
  m.setdefault(node, set())
  for next in allto.split():
    m.setdefault(next, set())
    m[node].add(next)
    m[next].add(node)

# Intution: run BFS from some node to all nodes. For each node remove
# edges in shortest path and repeat three times. If not all nodes are
# reachable by the third time, the graph was disconnected cutting three
# edges.
source = list(m.keys())[0] # pick any node as source
prev_map0, _ = bfs(source)
for node in m.keys():
  if node != source:
    # remove path to node
    to_ignore = to_edges(source, node, prev_map0)
    prev_map, _ = bfs(source, to_ignore)
    # remove path to node, second time
    to_ignore = to_ignore + to_edges(source, node, prev_map)
    prev_map, _ = bfs(source, to_ignore)
    # remove path to node, third time
    to_ignore = to_ignore + to_edges(source, node, prev_map)
    prev_map, n_reachable = bfs(source, to_ignore)
    if n_reachable != len(m):
      # components were disconnected
      print(n_reachable * (len(m)-n_reachable))
      break
