*-----Begin bootstrap-----
*-----Create Stack Frame-----
0:    LDC  6,1(6)    ;inc top by 1
1:     ST  1,0(6)    ;store r1 at top in dmem
2:    LDC  6,1(6)    ;inc top by 1
3:     ST  2,0(6)    ;store r2 at top in dmem
4:    LDC  6,1(6)    ;inc top by 1
5:     ST  3,0(6)    ;store r3 at top in dmem
6:    LDC  6,1(6)    ;inc top by 1
7:     ST  4,0(6)    ;store r4 at top in dmem
8:    LDC  5,0(0)    ;load numargs into r5
9:     ST  5,1(6)    ;store number args at top + 1 in dmem
10:    LDC  6,1(6)    ;inc top by 1
11:    ADD  5,1,7    ;add offset to pc
12:     ST  5,1(6)    ;store pc at top + 1 in dmem
13:    LDC  6,1(6)    ;inc top by 1
*-----End Stack Frame-----

*-----Begin function call-----
14:    LDC  1,1(0)    ;
15:    OUT  1,0,0    ;Printing value:1
*-----End function call-----

16:    OUT  6,0,0    ;print the return register
17:   HALT  0,0,0    ;
*-----End bootstrap-----

*-----Begin function call-----
18:    LDC  1,1(0)    ;
19:    OUT  1,0,0    ;Printing value:1
*-----End function call-----

*-----Begin function call-----
20:    LDC  1,1(0)    ;
21:    OUT  1,0,0    ;Printing value:1
*-----End function call-----

*-----Begin compute offsets-----
*-----End compute offsets-----

