var x
var y
var z
var t
str newline "\n"
move 1.0 x
move 2.0 y
move 3.14159 z
sys writer x
sys writes newline
sys writer z
sys writes newline
sys writer y
sys writes newline
move z r3
move 2.0 r4
divr r4 r3
move r3 x
move z r5
move y r6
divr r6 r5
move r5 y
sys writer x
sys writes newline
sys writer y
sys writes newline
move x r7
move y r8
addr r8 r7
move r7 t
sys writer t
sys writes newline
sys halt

