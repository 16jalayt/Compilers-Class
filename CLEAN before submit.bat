@ECHO OFF

del Compilers-Class.iml
del .gitignore
del run_test.bat
rmdir /S /Q examples
rmdir /S /Q out
rmdir /S /Q tools
rmdir /S /Q .idea
rmdir /S /Q .git
rmdir /S /Q Code

DEL "%~f0"