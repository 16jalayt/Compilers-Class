from fb_token import Token, TokenType
from scanner  import Scanner
from errors   import ParseError
# from ast    import *

# ---------------------------------
# non-terminals
# ---------------------------------

from enum import Enum

class NonTerminal(Enum):
    Program     = 0
    Range       = 1
    Assignments = 2
    Assignment  = 3
    Word        = 4
    Number      = 5

# ---------------------------------
# stack operators for grammar rules
# ---------------------------------

def top(stack):
    return stack[-1]

def pop(stack):
    stack.pop()

def push_rule(lst, stack):
    for element in reversed(lst):
        stack.append(element)

# ---------------------------------
# parse table
# ---------------------------------

parse_table = {
    (NonTerminal.Program,     TokenType.NUMBER) : [ NonTerminal.Range,
                                                    NonTerminal.Assignments ],
    (NonTerminal.Range,       TokenType.NUMBER) : [ NonTerminal.Number,
                                                    TokenType.ELLIPSIS,
                                                    NonTerminal.Number ],
    (NonTerminal.Assignments, TokenType.WORD)   : [ NonTerminal.Assignment,
                                                    NonTerminal.Assignments ],
    (NonTerminal.Assignments, TokenType.EOF)    : [],
    (NonTerminal.Assignment,  TokenType.WORD)   : [ NonTerminal.Word,
                                                    TokenType.EQUALS,
                                                    NonTerminal.Number ],
    (NonTerminal.Word,        TokenType.WORD)   : [ TokenType.WORD ],
    (NonTerminal.Number,      TokenType.NUMBER) : [ TokenType.NUMBER ]
}

# ---------------------------------
# parser
# ---------------------------------

class Parser:
    'Validate a stream of tokens'

    def __init__(self, scanner):
      self.scanner = scanner

    def parse(self):
        stack = []
        push_rule( [ NonTerminal.Program, TokenType.EOF ], stack)
        while stack:
            A = top(stack)
            if isinstance( A, TokenType ):
                t = self.scanner.next_token()
                if A == t.token_type:
                    pop(stack)
                else:
                    msg = 'token mismatch: {} and {}'
                    raise ParseError(msg.format(A,t))
            elif isinstance( A, NonTerminal ):
                t = self.scanner.peek()
                rule = parse_table.get( (A, t.token_type) )
                if rule is not None:
                    pop(stack)
                    push_rule(rule, stack)
                else:
                    msg = 'cannot expand {} on {}'
                    raise ParseError(msg.format(A,t))
            else:
                msg = 'invalid item on stack: {}'
                raise ParseError(msg.format(A))

        if not t.is_eof():
            msg = 'unexpected token at end: {}'
            raise ParseError(msg.format(t))

        return True
