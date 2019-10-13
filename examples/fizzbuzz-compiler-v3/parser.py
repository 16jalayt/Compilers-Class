from fb_token import Token, TokenType
from scanner  import Scanner
from ast      import *
from errors   import ParseError

class Parser:
  'Construct an abstract syntax tree from a stream of tokens'

  def __init__(self, scanner):
    self.scanner = scanner
   
  def parse(self):
    return self.parse_program()

  # --------------------------------------------------------

  def parse_program(self):
    range_node  = self.parse_range()
    assignments = self.parse_assignments()
    return Program_Node(range_node, assignments)

  def parse_range(self):
    lower_bound = self.match_number()
    self.match(TokenType.ELLIPSIS)
    upper_bound = self.match_number()
    return Range_Node(lower_bound, upper_bound)

  def parse_assignment(self):
    word = self.match_word()
    self.match(TokenType.EQUALS)
    value = self.match_number()
    return Assignment_Node(word, value)

  def parse_assignments(self, results = []):
    next_token = self.scanner.peek()
    if next_token.is_eof():
      return results
    elif next_token.is_word():
      assignment = self.parse_assignment()
      return self.parse_assignments( results + [assignment] )
    else:
      msg = 'expected assignment or EOF, but found {}'
      raise ParseError(msg.format(next_token))

  # --------------------------------------------------------

  def match(self, expected_token_type):
    next_token = self.scanner.next_token()
    if next_token.token_type == expected_token_type:
      return True
    else:
      msg = 'expected {} but found {}'.format(expected_token_type, \
                                              next_token.token_type)
      raise ParseError(msg)

  def match_number(self):
    next_token = self.scanner.next_token()
    if next_token.is_number():
      return next_token.value()
    else:
      msg = 'expected number but found {}'.format(
                                              next_token.token_type)
      raise ParseError(msg)

  def match_word(self):
    next_token = self.scanner.next_token()
    if next_token.is_word():
      return next_token.value()
    else:
      msg = 'expected word but found {}'.format(
                                              next_token.token_type)
      raise ParseError(msg)

