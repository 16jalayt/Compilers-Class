*-----Begin bootstrap-----
*-----End bootstrap-----

*-----Begin function call-----
0:    LDC  1,23(0)    ;Load number
1:    LDC  2,10(0)    ;Load number
2:    LDC  3,58(0)    ;Load number
3:    LDC  4,62(0)    ;Load number
4:    SUB  3,3,0     ;subtract operands to compare to 0
5:    JEQ  3,3(0)    ;testing equal
*-----End function call-----

*-----Ending Program-----

6:    OUT  0,0,0     ;print the return register
7:   HALT  0,0,0     ;
*-----Begin compute offsets-----
*-----End compute offsets-----

