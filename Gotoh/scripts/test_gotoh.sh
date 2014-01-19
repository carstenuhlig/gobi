#!/bin/bash

# Evaluierung 1
# abgaben verzeichnis stand 19.12.13: /home/proj/biocluster/praktikum/genprakt-ws13/abgaben/assignment1

# GOBI_GOTOH_DIR=/home/proj/biocluster/praktikum/genprakt-ws13/abgaben/assignment1/
GOBI_GOTOH_DIR=./
# BENCHMARK_DIR=/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/
BENCHMARK_DIR=../original_res/
MODE=( freeshift global local )

# for m in MODE; do
# 	java -Xms2G -Xmx4G -jar $GOBI_GOTOH_DIRuhligc/gotoh.jar -pairs $BENCHMARK_DIR/cathscop.inpairs -seqlib $BENCHMARK_DIR/domains.seqlib -mode $MODE -printali
# done

# AUSGABE=uhligc_$MODE.out

for m in ${MODE[@]}; do
	AUSGABE=uhligc_${m}.out
	echo Generating $AUSGABE
	java -jar ${GOBI_GOTOH_DIR}Gotoh.jar -pairs ${BENCHMARK_DIR}sanity.pairs -seqlib ${BENCHMARK_DIR}domains.seqlib -mode $MODE -printali > $AUSGABE
done

#exit 0
# noch keine richtige Implementierung von Matrix input

# Evaluierung 2
GOTOH_REFERENCE=../original_res/gotoh
DAYHOFF_MATRIX=../original_res/matrices/dayhoff.mat
echo check now

for m in ${MODE[@]}; do
	AUSGABE=uhligc_${m}.out
	$GOTOH_REFERENCE -mode $MODE -check $AUSGABE –matrix $DAYHOFF_MATRIX > $AUSGABE.check 2>&1
done

echo finished

# for name in ${names[@]}
# do
# 	echo $name
# # other stuff on $name
# done