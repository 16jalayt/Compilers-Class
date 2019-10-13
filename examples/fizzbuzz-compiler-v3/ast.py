# root class for all AST nodes
class AST_Node(object):
    pass

# concrete AST classes
class Program_Node(AST_Node):
  def __init__(self, range, assignments):
    self._range       = range
    self._assignments = assignments

  def range(self):
    return self._range

  def assignments(self):
    return self._assignments

  def pretty_print(self):
    result  = 'Write a program that prints the numbers '
    result += self.range().pretty_print()
    result += '.  However:'
    for assign in self.assignments().value():
      result += assign.pretty_print()
    result += '  If a number is divisible by more than one of the'
    result += ' divisors, print all corresponding words in order.'
    return result

class Range_Node(AST_Node):
  def __init__(self, lower, upper):
    self.lower_bound = lower
    self.upper_bound = upper

  def lower(self):
    return self.lower_bound

  def upper(self):
    return self.upper_bound

  def is_valid(self, n):
    return self.lower() < n and n < self.upper()

  def pretty_print(self):
    return '{} through {}'.format(self.lower(), self.upper())

  def __str__(self):
    return '{} ... {}'.format(self.lower(), self.upper())

class Assignment_Node(AST_Node):
  def __init__(self, word, value):
    self._word  = word
    self._value = value

  def word(self):
    return self._word

  def value(self):
    return self._value

  def pretty_print(self):
    word_str = '  For every number divisible by {}, print "{}".'
    return word_str.format(self.value(), self.word())

  def __str__(self):
    return '{} = {}'.format(self.word(), self.value())

class Assignment_List(AST_Node):
  def __init__(self):
    self._list  = []

  def add(self, assignment):
    return self._list.insert(0, assignment)

  def get(self, i):
    return self._list[i]

  def value(self):
    return self._list

  def pretty_print(self):
    word_str = ''
    for item in self._list:
        word_str += item.pretty_print()
    return word_str

  def __str__(self):
    word_str = ''
    for item in self._list:
        word_str += str(item)
    return word_str

