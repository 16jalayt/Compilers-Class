from fb_token import Token, TokenType
from scanner  import Scanner
from errors   import ParseError
from ast      import *

from enum     import Enum

# ---------------------------------
# stack operators for grammar rules
# ---------------------------------

def top(stack):
    return stack[-1]

def pop(stack):
    return stack.pop()                # return value !

def push(element, stack):
    stack.append(element)

def push_rule(lst, stack):
    for element in reversed(lst):
        stack.append(element)

# ---------------------------------
# non-terminals
# ---------------------------------

class NonTerminal(Enum):
    Program     = 0
    Range       = 1
    Assignments = 2
    Assignment  = 3
    Word        = 4
    Number      = 5

# ---------------------------------
# semantic actions
# ---------------------------------

class AstAction(Enum):
    MakeProgram     = 0
    MakeRange       = 1
    MakeAssignment  = 2
    MakeAssignments = 3

# ---------------------------------
# semantic action table
# ---------------------------------

def make_program_node(ast_stack):
    assignment_list = pop(ast_stack)
    range           = pop(ast_stack)
    new_node = Program_Node(range, assignment_list)
    push(new_node, ast_stack)

def make_range_node(ast_stack):
    upper = pop(ast_stack)
    lower = pop(ast_stack)
    new_node = Range_Node(lower, upper)
    push(new_node, ast_stack)

def make_assignment_node(ast_stack):
    value = pop(ast_stack)
    word  = pop(ast_stack)
    new_node = Assignment_Node(word, value)
    push(new_node, ast_stack)

def make_assignments_node(ast_stack):
    list_of_assignments = Assignment_List()
    while isinstance(top(ast_stack), Assignment_Node):
        list_of_assignments.add(pop(ast_stack))
    push(list_of_assignments, ast_stack)

action_table = {
    AstAction.MakeProgram     : make_program_node,
    AstAction.MakeRange       : make_range_node,
    AstAction.MakeAssignment  : make_assignment_node,
    AstAction.MakeAssignments : make_assignments_node
}

# ---------------------------------
# parse table
# ---------------------------------

parse_table = {
    (NonTerminal.Program,     TokenType.NUMBER) : [ NonTerminal.Range,
                                                    NonTerminal.Assignments,
                                                    AstAction.MakeProgram  ],
    (NonTerminal.Range,       TokenType.NUMBER) : [ NonTerminal.Number,
                                                    TokenType.ELLIPSIS,
                                                    NonTerminal.Number,
                                                    AstAction.MakeRange ],
    (NonTerminal.Assignments, TokenType.WORD)   : [ NonTerminal.Assignment,
                                                    NonTerminal.Assignments ],
    (NonTerminal.Assignments, TokenType.EOF)   : [ AstAction.MakeAssignments ],
    (NonTerminal.Assignment,  TokenType.WORD)   : [ NonTerminal.Word,
                                                    TokenType.EQUALS,
                                                    NonTerminal.Number,
                                                    AstAction.MakeAssignment ],
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
        parse_stack = []
        semantic_stack = []
        push_rule( [ NonTerminal.Program, TokenType.EOF ], parse_stack)
        while parse_stack:
            A = top(parse_stack)
            if isinstance( A, TokenType ):
                t = self.scanner.next_token()
                if A == t.token_type:
                    pop(parse_stack)
                    if t.is_number() or t.is_word():
                        push(t.value(), semantic_stack)
                else:
                    msg = 'token mismatch: {} and {}'
                    raise ParseError(msg.format(A,t))
            elif isinstance( A, NonTerminal ):
                t = self.scanner.peek()
                rule = parse_table.get( (A, t.token_type) )
                if rule is not None:
                    pop(parse_stack)
                    push_rule(rule, parse_stack)
                else:
                    msg = 'cannot expand {} on {}'
                    raise ParseError(msg.format(A,t))
            elif isinstance( A, AstAction ):
                action = action_table.get(A)
                action(semantic_stack)
                pop(parse_stack)
            else:
                msg = 'invalid item on parse stack: {}'
                raise ParseError(msg.format(A))

        if not t.is_eof():
            msg = 'unexpected token at end: {}'
            raise ParseError(msg.format(t))

        if len(semantic_stack) != 1:
            msg = 'unexpected number of AST nodes: {}'
            raise ParseError(msg.format(semantic_stack))

        return top(semantic_stack)

