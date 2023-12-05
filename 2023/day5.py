import sys

def next_mapping():
    sys.stdin.readline() # map header
    map = []
    while True:
        line = sys.stdin.readline().strip()
        if line == "":
            break
        d,s,l = (int(n) for n in line.split()) # (dest start, source start, source end)
        map.append((s,s+l,d))
    map.sort()
    return map

def get_next(source, mapping):
    for source_start, source_end, dest_start in mapping:
        if source_start <= source and source < source_end:
            return dest_start + (source - source_start)
    return source # same number

# map given range [s_start, s_end] to next ranges based on its mapping.
# A range can map to multiple ranges, depending on its mapping ranges.
# This method returns a list of ranges this maps to.
def get_next2(s_start, s_end, mapping):
    sid = 0
    for sid, (source_start, source_end, dest_start) in enumerate(mapping):
        if s_start <= source_end:
            # first overlapping mapping
            break
        sid += 1
    # go through all mappings computing the new destinations
    ranges = []
    last_end = s_start
    for id in range(sid, len(mapping)):
        (source_start, source_end, dest_start) = mapping[id]
        if source_start > s_end:
            break
        if last_end < source_start:
            # maps to the same range, continue
            ranges.append((last_end, min(s_end, source_start)))
            last_end = min(s_end, source_start)
        if last_end == s_end:
            # the end
            break
        new_end = min(s_end, source_end)
        ds=map_to(last_end, source_start, dest_start)
        de=map_to(new_end, source_start, dest_start)
        ranges.append((ds, de))
        last_end = new_end
    if last_end != s_end:
        ranges.append((last_end, s_end))
    return ranges

def map_to(source, source_start, dest_start):
    return dest_start + (source - source_start)

seeds = [int(s) for s in sys.stdin.readline().strip().split(':')[1].strip().split()]
sys.stdin.readline() # empty

seed_to_soil = next_mapping()
soil_to_fertilizer = next_mapping()
fertilizer_to_water = next_mapping()
water_to_light = next_mapping()
light_to_temperature = next_mapping()
temperature_to_humidity = next_mapping()
humidity_to_location = next_mapping()

min_s = float('inf')
for s in seeds:
    for mapping in [seed_to_soil, soil_to_fertilizer, fertilizer_to_water, water_to_light, light_to_temperature, temperature_to_humidity, humidity_to_location]:
        s = get_next(s, mapping)
    min_s = min(min_s, s)
print(min_s)

min_s = float('inf')
for i in range(0, len(seeds), 2):
    s = [(seeds[i], seeds[i] + seeds[i+1])]
    for mapping in [seed_to_soil, soil_to_fertilizer, fertilizer_to_water, water_to_light, light_to_temperature, temperature_to_humidity, humidity_to_location]:
        next = []
        for (s0, s1) in s:
            next.extend(get_next2(s0, s1, mapping))
        s = next
    for (s0, s1) in s:
        min_s = min(min_s, s0)

print(min_s)
