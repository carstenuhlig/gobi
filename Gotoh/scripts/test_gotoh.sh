#!/bin/bash

# Evaluierung 1
# abgaben verzeichnis stand 19.12.13: /home/proj/biocluster/praktikum/genprakt-ws13/abgaben/assignment1

GOBI_GOTOH_DIR=/home/u/uhligc/git/gobi/Gotoh/scripts
BENCHMARK_DIR=/home/u/uhligc/git/gobi/Gotoh/original_res
MODE=( global freeshift local )

# for m in MODE; do
# 	java -Xms2G -Xmx4G -jar $GOBI_GOTOH_DIRuhligc/gotoh.jar -pairs $BENCHMARK_DIR/cathscop.inpairs -seqlib $BENCHMARK_DIR/domains.seqlib -mode $MODE -printali
# done

# AUSGABE=uhligc_$MODE.out
echo === Gen Alignments ===
echo

for m in ${MODE[@]}; do
	AUSGABE=$GOBI_GOTOH_DIR/uhligc_${m}.out
    echo Gen uhligc_${m}.out ${GOBI_GOTOH_DIR}/Gotoh.jar
	java -Xms2G -Xmx2G -jar ${GOBI_GOTOH_DIR}/Gotoh.jar -pairs $BENCHMARK_DIR/cathscop.inpairs -seqlib $BENCHMARK_DIR/domains.seqlib -mode $m -printali > $AUSGABE
done

echo === Gen Finished ===

#exit 0
# noch keine richtige Implementierung von Matrix input

# Evaluierung 2
GOTOH_REFERENCE=/home/u/uhligc/git/gobi/Gotoh/original_res/gotoh
DAYHOFF_MATRIX=/home/u/uhligc/git/gobi/Gotoh/original_res/matrices/dayhoff.mat

for m in ${MODE[@]}; do
	AUSGABE=$GOBI_GOTOH_DIR/uhligc_${m}.out
    echo Gen uhligc_${m}.out with "$GOTOH_REFERENCE -mode $m -check $AUSGABE -matrix $DAYHOFF_MATRIX"
	$GOTOH_REFERENCE -mode $m -check $AUSGABE -matrix $DAYHOFF_MATRIX > $AUSGABE.check 2>&1
done

# for name in ${names[@]}
# do
# 	echo $name
# # other stuff on $name
# done
