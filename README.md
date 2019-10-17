# Compilers-Class
Team Argonaut: Layton*, Litterer, Riggs

Git notes:
To create project, click checkout from git, then paste in https://github.com/16jalayt/Compilers-Class.git

Make sure to use sync when starting.
Use commit when done with chunks of code.
MAKE SURE YOU PUSH CHANGES and they show up in the repo.

Classes in this file:
Main(): Takes in a klein file name, calls Scanner.
Token(): Creates the token object type, which has a token.type (keyword, identifier, punctuation, etc.)
         and token.value which is the token itself as a string.
Scanner(): Takes in a string containing a klein program. Produces tokens from that string. Calls token.
Parser(): Uses scanner() to take in token after token from a given program, comparing them to Klein grammar rules to 
          determine whether or not the program is valid in klein. If the parser reaches the end of the file, finds
          the End Of File character, and has not encounter any errors, it returns an AST.
Node(): Our custom node class. Contains an Iterate() method that iterates through and prints out an AST.

How to Use:
Our main function takes in a single parameter as input, that being a klein file name as a string. 
Main then opens that file and reads its contents into a single large string, then it passes that string to 
our scanner function.
Our scanner function takes in a single parameter, a string containing the contents of a klein program. 
Scanner then goes through that string character by character, building tokens as it goes. 
Main then calls Parser.parse(), and if it returns true then Main prints out "The program is: valid", else it
    declares that the program is invalid.
    
To build: The repo comes with an intellij project. Launching the project is the fastest way to get started. 
Alternativly, you can cd into the src/src folder and compile with javac Main.java Scanner.java Token.java... 
and so on for all of the source files in the directory. To lauch, you call java Main test.kln where test is 
the klein program with path.

Current Problems: Our parser currently has a problem where it attempts to call a rule that does not exist
    in our Parser Table. We believe that this is caused by a mistake in our FIRST and FOLLOW file, which 
    lead to the mistake being present in our Parser Table and finally, our code that implemented the table.
    We were unable to find our error before the due date, so parser unfortunately does not make it all of the
    way through every klein file.