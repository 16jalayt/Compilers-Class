# Compilers-Class
A group project for College

Git notes:
To create project, click checkout from git, then paste in https://github.com/16jalayt/Compilers-Class.git

Make sure to use sync when starting.
Use commit when done with chunks of code.
MAKE SURE YOU PUSH CHANGES and they show up in the repo.

If dead state should print message and terminate

Classes in this file:
Main(): takes in a klein file name, calls Scanner.
Scanner(): takes in a string containing a klein program. Produces tokens from that string. Calls token.
Token(): Creates the token object type, which has a token.type (keyword, identifier, punctuation, etc.)
         and token.value which is the token itself as a string. 

How to Use:
Our main function takes in a single parameter as input, that being a klein file name as a string. 
Main then opens that file and reads its contents into a single large string, then it passes that string to 
our scanner function.
Our scanner function takes in a single parameter, a string containing the contents of a klein program. 
Scanner then goes through that string character by character, building tokens as it goes. 
Main will print out the tokens produced by scanner, ending once scanner produces and End of File character OR
if scanner produces an illegal token.

Status/ Current Problems:
Currently our scanner works, but we are having issues with it identifying tabs as whitespaces. 