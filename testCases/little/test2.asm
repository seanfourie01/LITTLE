var a
var b
var c
var d
move 20 a
move 30 b
move 40 c
move a r3
move b r4
subi r4 r3
move r3 c
move b r5
move b r6
muli r6 r5
move r5 b
move b r7
move a r8
addi r8 r7
move r7 b
move b r9
move a r10
muli r10 r9
move r9 d
move d r11
move a r12
divi r12 r11
move r11 a
sys writei c
sys writei b
sys writei a
sys halt

