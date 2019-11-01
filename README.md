# Compilers-Class
Team Argonaut: Layton*, Litterer, Riggs

Git notes:
To create project, click checkout from git, then paste in https://github.com/16jalayt/Compilers-Class.git

Make sure to use sync when starting.
Use commit when done with chunks of code.
MAKE SURE YOU PUSH CHANGES and they show up in the repo.

Classes in this file:
Main(): Has multiple debug stages. debug=1 is for testing scanner, debug=3 is for testing parser true/false, 
        debug=5 is for testing Parser AST return, and debug=6 is for testing full functionality.
Token(): Creates the token object type, which has a token.type (keyword, identifier, punctuation, etc.)
         and token.value which is the token itself as a string.
Scanner(): Takes in a string containing a klein program. Produces tokens from that string. Calls token.
Parser(): Uses scanner() to take in token after token from a given program, comparing them to Klein grammar rules to 
          determine whether or not the program is valid in klein. If the parser reaches the end of the file, finds
          the End Of File character, and has not encounter any errors, it returns an AST.
Node(): Our custom node class. Contains an Iterate() method that iterates through and prints out an AST. All nodes
        have a name associated with them, some have values as well.
TypeChecker(): Goes through the AST from the Parser and makes sure it is semantically correct. If incorrect,
               it lets the user know where the error comes frome.
TT_Obj(): This is the data structure used for each function named in the progam. We want to keep track of the formals, type,
           what functions it called and what functions calls it. 


How to Use:
Our main function takes in a single parameter as input, that being a klein file name as a string. 
Main then opens that file and reads its contents into a single large string, then it passes that string to 
    our scanner function.
Our scanner function takes in a single parameter, a string containing the contents of a klein program. 
Scanner then goes through that string character by character, building tokens as it goes. 
Main then calls Parser.parse(), and if it returns true then Main it calls Parser.getTree(), which
prints out our programs Abstract Syntax Tree of nodes, with their names, and any values that they may hold.
The TypeChecker is then run through the AST to ensure semantic correctness and create a symbol table. 
    
To build: The repo comes with an intellij project. Launching the project is the fastest way to get started. 
Alternatively, you can cd into the src/src folder and compile with javac Main.java Scanner.java Token.java... 
and so on for all of the source files in the directory. To launch, you call java Main test.kln where test is 
the klein program with path.

Current Problems: It's hard to pin down exactly, but there is a combination of failueres from the TypeChecker in 
                  comparing the types as well when the hashmap symbol table with a key that's the def identifier and its
                  pair that is a TT_Object. The main issue comes from the functionCall. As a result we didn't get to finishing
                  much else afterwards. So while there may be comparison of node types, they don't always set what should be
                  in the symbol table.  