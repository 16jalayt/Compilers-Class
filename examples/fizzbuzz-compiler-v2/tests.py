import unittest
from fb_token     import src.Token, TokenType
from scanner      import src.Scanner
from ast          import *
from parser       import src.Parser
from type_checker import TypeChecker
from errors       import LexicalError, ParseError, SemanticError

class ScannerTestCases(unittest.TestCase):

  def test_peek_past_whitespace(self):
    '''Find literal past whitespace.'''
    s = src.Scanner('     ...= ')
    self.assertTrue(s.peek().is_ellipsis(), 'whitespace before ...')

  def test_literal_tokens_with_whitespace(self):
    '''Find two literals inside whitespace.'''
    s = src.Scanner('     ...= ')
    self.assertTrue(s.next_token().is_ellipsis())
    self.assertTrue(s.next_token().is_equals())
    self.assertTrue(s.next_token().is_eof())

  def test_word_past_whitespace(self):
    '''Find word past whitespace.'''
    s = src.Scanner('   aBc ')
    self.assertTrue(s.peek().is_word(),      'peek past whitespace')
    
    next_token = s.next_token()
    self.assertTrue (next_token.is_word(),   'right token type')
    self.assertEqual(next_token.value(),     'aBc', 'right token value')
    
    self.assertTrue(s.next_token().is_eof(), 'EOF after word')

  def test_word_within_iterals(self):
    '''Find word within two literals.'''
    s = src.Scanner('...aBc=')
    self.assertTrue(s.next_token().is_ellipsis())
    self.assertTrue(s.next_token().is_word())
    self.assertTrue(s.next_token().is_equals())

  def test_two_words(self):
    '''Find two words.'''
    s = src.Scanner(' eugene wallingford')
    next_token = s.next_token()
    self.assertTrue (next_token.is_word(), 'first token right')
    self.assertEqual(next_token.value(),   'eugene')
    next_token = s.next_token()
    self.assertTrue (next_token.is_word(), 'second token right')
    self.assertEqual(next_token.value(),   'wallingford')

  def test_one_number(self):
    '''Find number.'''
    s = src.Scanner('42')
    next_token = s.next_token()
    self.assertTrue (next_token.is_number(), 'found right token')
    self.assertEqual(next_token.value(), 42)

  def test_two_numbers(self):
    '''Find two numbers in whitespace.'''
    s = src.Scanner(' 3540  \n\t    4550      ')
    next_token = s.next_token()
    self.assertTrue (next_token.is_number(), 'found right token')
    self.assertEqual(next_token.value(), 3540)
    next_token = s.next_token()
    self.assertTrue (next_token.is_number(), 'found right token')
    self.assertEqual(next_token.value(), 4550)

  def test_assignment(self):
    '''Recognize tokens in an assignment statement.'''
    s = src.Scanner(' fizz=3\n')
    next_token = s.next_token()
    self.assertTrue (next_token.is_word(), 'first token right')
    self.assertEqual(next_token.value(), 'fizz')
    self.assertTrue(s.next_token().is_equals())
    next_token = s.next_token()
    self.assertTrue (next_token.is_number(), 'found right token')
    self.assertEqual(next_token.value(), 3)

  def test_loop_spec(self):
    '''Recognize tokens in a loop specification.'''
    s = src.Scanner(' 1...100\t')
    next_token = s.next_token()
    self.assertTrue (next_token.is_number(), 'found right token')
    self.assertEqual(next_token.value(), 1)
    self.assertTrue(s.next_token().is_ellipsis())
    next_token = s.next_token()
    self.assertTrue (next_token.is_number(), 'found right token')
    self.assertEqual(next_token.value(), 100)

class ParserTestCases(unittest.TestCase):

  def test_match_literal_tokens(self):
    '''Match literal tokens.'''
    s = src.Scanner('     ...= ')
    p = src.Parser(s)
    self.assertTrue(p.match(TokenType.ELLIPSIS))
    self.assertTrue(p.match(TokenType.EQUALS))

  def test_fail_to_match_literal_tokens(self):
    '''Match literal tokens.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('     ...= ')
      p = src.Parser(s)
      p.match(TokenType.EQUALS)

  def test_parse_range_succeeds(self):
    '''Parse a valid range.'''
    s = src.Scanner('1...15')
    p = src.Parser(s)
    fb_range = p.parse_range()
    self.assertTrue(isinstance(fb_range, Range_Node))
    self.assertEqual(fb_range.lower(), 1)
    self.assertEqual(fb_range.upper(), 15)

  def test_parse_range_fails_on_lower_bound(self):
    '''Fail on an invalid range: bad lower bound.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('Buzz...15')
      p = src.Parser(s)
      fb_range = p.parse_range()

  def test_parse_range_fails_on_ellipsis(self):
    '''Fail on an invalid range.: missing the ellipsis.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('1=15')
      p = src.Parser(s)
      fb_range = p.parse_range()

  def test_parse_range_fails_on_upper_bound(self):
    '''Fail on an invalid range: bad upper bound.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('1...Fizz')
      p = src.Parser(s)
      fb_range = p.parse_range()

  def test_parse_assignment_succeeds(self):
    '''Parse a valid assignment.'''
    s = src.Scanner('fizz = 1')
    p = src.Parser(s)
    assignment = p.parse_assignment()
    self.assertTrue(isinstance(assignment, Assignment_Node))
    self.assertEqual(assignment.word(), 'fizz')
    self.assertEqual(assignment.value(), 1)

  def test_parse_assignment_fails_on_word(self):
    '''Fail on an invalid range: invalid word.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('1 = 15')
      p = src.Parser(s)
      fb_range = p.parse_assignment()

  def test_parse_assignment_fails_on_equal_sign(self):
    '''Fail on an invalid range.: missing the equal sign.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('buzz...15')
      p = src.Parser(s)
      fb_range = p.parse_assignment()

  def test_parse_assignment_fails_on_value(self):
    '''Fail on an invalid range: bad upper bound.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('buzz=...')
      p = src.Parser(s)
      fb_range = p.parse_assignment()

  def test_parse_assignments_succeeds(self):
    '''Parse a valid set of assignment.'''
    s = src.Scanner('fizz = 3\nbuzz=5')
    p = src.Parser(s)
    assignment_list = p.parse_assignments()
    self.assertTrue(isinstance(assignment_list, list))
    self.assertEqual(assignment_list[0].word(), 'fizz')
    self.assertEqual(assignment_list[0].value(), 3)
    self.assertEqual(assignment_list[1].word(), 'buzz')
    self.assertEqual(assignment_list[1].value(), 5)

  def test_parse_assignments_fails_on_next_token(self):
    '''Fail on an invalid range: bad upper bound.'''
    with self.assertRaises(ParseError) as context:
      s = src.Scanner('fizz = 3\nbuzz=5\n42')
      p = src.Parser(s)
      fb_range = p.parse_assignments()

  def test_parse_program_succeeds(self):
    '''Parse a valid program.'''
    s = src.Scanner('1...15\nfizz=3\nbuzz=5')
    p = src.Parser(s)
    program = p.parse_program()
    self.assertTrue(isinstance(program, Program_Node))
    self.assertTrue(isinstance(program.range(), Range_Node))
    self.assertTrue(isinstance(program.assignments(), list))
    for a in program.assignments():
      self.assertTrue(isinstance(a, Assignment_Node))

class TypeCheckerTestCases(unittest.TestCase):

  def test_check_valid_program_default(self):
    '''Pass for a valid program.'''
    s  = src.Scanner('1...15\nfizz=3\nbuzz=5')
    p  = src.Parser(s)
    tc = TypeChecker(p.parse_program())
    st = tc.type_check()
    self.assertTrue(isinstance(st, dict))
    self.assertTrue(3 in st)
    self.assertTrue(5 in st)
    self.assertTrue('fizz' in st.values())
    self.assertTrue('buzz' in st.values())

  def test_check_valid_program_extended(self):
    '''Pass for a valid program.'''
    s  = src.Scanner('1...15\nfizz=3\nbuzz=5 biff=7 boff=11')
    p  = src.Parser(s)
    tc = TypeChecker(p.parse_program())
    st = tc.type_check()
    self.assertTrue(isinstance(st, dict))
    self.assertTrue(3 in st)
    self.assertTrue(5 in st)
    self.assertTrue(7 in st)
    self.assertTrue(11 in st)
    self.assertTrue('fizz' in st.values())
    self.assertTrue('buzz' in st.values())
    self.assertTrue('biff' in st.values())
    self.assertTrue('boff' in st.values())

  def test_check_valid_range(self):
    '''Pass for the range on a valid program.'''
    s  = src.Scanner('1...15\nfizz=3\nbuzz=5')
    p  = src.Parser(s)
    tc = TypeChecker(p.parse_program())
    self.assertTrue(tc.check_range())

  def test_check_range_fails_on_upper_bound(self):
    '''Fail because upper bound is not > lower bound.'''
    with self.assertRaises(SemanticError) as context:
      s  = src.Scanner('15...1\nfizz=3\nbuzz=5')
      p  = src.Parser(s)
      tc = TypeChecker(p.parse_program())
      tc.check_range()

  def test_check_assignmentsfails_on_words_value_out_of_range(self):
    '''Fail because lower bound is not > 0.'''
    with self.assertRaises(SemanticError) as context:
      s  = src.Scanner('1...15\nfizz=32\nbuzz=5')
      p  = src.Parser(s)
      tc = TypeChecker(p.parse_program())
      tc.check_assignments()

  def test_check_assignments_fails_on_duplicate_words(self):
    '''Fail because a word is used twice.'''
    with self.assertRaises(SemanticError) as context:
      s  = src.Scanner('1...15\nfizz=3\nfizz=5')
      p  = src.Parser(s)
      tc = TypeChecker(p.parse_program())
      tc.check_assignments()

if __name__ == '__main__':
  unittest.main()          # run all tests

# features for use later -----------------------------
#
# def suite():
#   suite = unittest.TestSuite()
#   suite.addTest(WidgetTestCase('test_default_size'))
#   suite.addTest(WidgetTestCase('test_resize'))
#   return suite
#
# def setUp(self):
#     """Call before every test case."""
#     self.foo = Foo()
#     self.file = open( "blah", "r" )
#
# def tearDown(self):
#     """Call after every test case."""
#     self.file.close()

