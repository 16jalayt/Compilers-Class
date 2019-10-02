import sys
from scanner      import Scanner
from fb_token     import Token
from parser       import Parser
from type_checker import TypeChecker
from codegen      import PythonGenerator

filename = sys.argv[1]
myfile   = open(filename)
program  = myfile.read()

scanner  = Scanner(program)
parser   = Parser(scanner)
gen      = PythonGenerator()

tree     = parser.parse()
# print( tree, '\n' )
# print( tree.pretty_print() )

checker  = TypeChecker(tree)
sym_tab  = checker.type_check()
program  = gen.generate(tree, sym_tab)

print(program)
