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
muli r4 r4
move a r8
addi r8 r4
move r4 b
move a r10
muli r10 r4
move a r12
divi r12 r4
move r4 a
sys writei c
sys writei b
sys writei a
sys halt

