0:     LDC 0,3(0) r0 val 3
1:     LDC 1,4(0) r1 val 4
2:     ADD 0,0,1 load into r2 the value of r0 + r1. 
*Can load the answer back into r0 since we don't need the value again.
3:     OUT 0,0,0
4:     HALT 0,0,0