0:     LD 4,1(0)  the first command line arg is in memory pos 1
1:     LDA 6,1(7)  store return in r6
2:     LDA 7,5(0) Jump to line r0 + 5
3:     OUT 4,0,0
4:     HALT 0,0,0
5:     MUL 4,4,4
6:     LDA 7,0(6) load the return addr into pc to jump back