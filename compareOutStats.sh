./Micro.sh testCases/inputs/"$1".micro > testCases/little/"$1".asm

./tinyNew ./testCases/little/"$1".asm
echo "\n----------\n"
./tinyNew ./testCases/outputs/"$1".out
