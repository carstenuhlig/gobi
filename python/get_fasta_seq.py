__author__ = 'carsten'
import sys

seqlibfile = open(sys.argv[1],'r')
proteinlistfile = open(sys.argv[2],'r')
ps_scanlistfile = open(sys.argv[3],'w')

seqlib = {}

for line in seqlibfile:
    seqlib[line.split(':')[0]] = seqlib[line.split(':')[1]]

seqlibfile.close()

for line in proteinlistfile:
    if seqlib.has_key(line):
        ps_scanlistfile.write(">" + line + "\n" + seqlib[line] + "\n")

proteinlistfile.close()