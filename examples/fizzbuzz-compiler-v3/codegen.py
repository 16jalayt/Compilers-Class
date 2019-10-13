import ast

# root class for all code generators
class CodeGenerator(object):
  def generate(self, program, symbol_table):
    msg = 'code generator must implement generate(ast, symbol_table)'
    raise NotImplementedError(msg)

# concrete class to produce a Python program
class PythonGenerator(CodeGenerator):

  def generate(self, program, symbol_table):
    start = program.range().lower()
    end   = program.range().upper() + 1
    prolog = self.generate_prolog(start, end)
    middle = self.generate_cases(symbol_table)
    epilog = '  print(output)'
    return prolog + middle + epilog

  # --------------------------------------------------------
    
  def target(self):
    return self._out_stream

  def generate_prolog(self, start, end):
    lines = 'for i in range({}, {}):\n  output = ""\n'
    return lines.format(start, end)

  def generate_cases(self, symbol_table):
    answer = ''
    for val in sorted(symbol_table.keys()): # symbol_table:
      case_str  = '  if (i % {}) == 0:\n    output += "{}"\n'
      answer   += case_str.format(val, symbol_table[val])
    answer += '  if output == "":\n    output += str(i)\n'
    return answer

##  def generate_cases(self, symbol_table):
##    first_case = True
##    answer = ''
##    for val in symbol_table:
##      if first_case:
##        prefix = '  '
##        first_case = False
##      else:
##        prefix = '  el'
##      case_str = prefix + 'if i == {}:\n    print("{}")\n'
##      answer   += case_str.format(val, symbol_table[val])
##    return answer
