import re, sys

map = {
  "one":   1,
  "two":   2,
  "three": 3,
  "four":  4,
  "five":  5,
  "six":   6,
  "seven": 7,
  "eight": 8,
  "nine":  9,
}

def extract_digits(input_string):
  matches = re.findall(r'(?=(\d|one|two|three|four|five|six|seven|eight|nine))', input_string)
  first_digit = map[matches[0]] if matches[0] in map else int(matches[0])
  last_digit = map[matches[-1]] if matches[-1] in map else int(matches[-1])
  return 10 * first_digit + last_digit

s = 0
for line in sys.stdin:
  s += extract_digits(line.strip())
print(s)

