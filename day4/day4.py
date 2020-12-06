import re
import sys

def matches(pattern, input, lo, hi):
    m = re.match(pattern, input)
    return m and lo <= int(m.group(1)) <= hi


d4_re = '^(\d{4})$'
hgt_cm_re = '^(\d+)cm$'
hgt_in_re = '^(\d+)in$'
hcl_re = '^#[0-9a-f]{6}$'
ecl_re = '^(amb|blu|brn|gry|grn|hzl|oth)$'
pid_re = '^\d{9}$'

passports = [{}]
for line in sys.stdin:
    if len(line.strip()) == 0:
        passports.append({})
        continue
    for tok in line.split(' '):
        kv = tok.split(':')
        passports[-1][kv[0]] = kv[1]

valid = 0
for pp in passports:
    if not ('byr' in pp and matches(d4_re, pp['byr'], 1920, 2002)):
        continue
    if not ('iyr' in pp and matches(d4_re, pp['iyr'], 2010, 2020)):
        continue
    if not ('eyr' in pp and matches(d4_re, pp['eyr'], 2020, 2030)):
        continue
    if not ('hgt' in pp and (matches(hgt_cm_re, pp['hgt'], 150, 193) or matches(hgt_in_re, pp['hgt'], 59, 76))):
        continue
    if not ('hcl' in pp and re.match(hcl_re, pp['hcl'])):
        continue
    if not ('ecl' in pp and re.match(ecl_re, pp['ecl'])):
        continue
    if not ('pid' in pp and re.match(pid_re, pp['pid'])):
        continue
    valid += 1

print(valid)
