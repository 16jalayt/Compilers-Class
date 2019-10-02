# from fb_token import Token, TokenType
# from scanner  import Scanner
from ast      import *
from errors   import SemanticError

class TypeChecker:
  'Type-check an AST and construct a symbol table.'

  def __init__(self, ast):
    self._ast = ast

  def ast(self):
    return self._ast
   
  def type_check(self):
    self.check_range()
    return self.check_assignments()

  # --------------------------------------------------------

  def check_range(self):
    # These violations of the Law of Demeter make me queasy.
    lower_bound = self.ast().range().lower()
    upper_bound = self.ast().range().upper()
    if not lower_bound < upper_bound:
      msg = 'lower bound {} must < upper bound {}'
      raise SemanticError(msg.format(lower_bound, upper_bound))
    return True

  def check_assignments(self):
    # These violations of the Law of Demeter make me queasy.
    program_range = self.ast().range()
    list_of_pairs = self.ast().assignments()
    
    symbol_table = {}
    for pair in list_of_pairs:
      if not program_range.is_valid(pair.value()):
        msg = 'value {} out of range in {}'
        raise SemanticError(msg.format(pair.value(), program_range))
      if pair.word() in symbol_table.values():
        msg = 'duplicate word {} in {}'
        raise SemanticError(msg.format(pair.word(), pair))
      symbol_table[pair.value()] = pair.word()
    return symbol_table

