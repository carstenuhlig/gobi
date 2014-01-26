__author__ = 'carsten'
import sys

seqlibfile = open(sys.argv[1],'r')
proteinlistfile = open(sys.argv[2],'r')
ps_scanlistfile = open(sys.argv[3],'w')

seqlib = {}

for line in seqlibfile:
    seqlib[line.split(':')[0]] = line.split(':')[1]

seqlibfile.close()

for line in proteinlistfile:
    id = line.rstrip()
    if seqlib.has_key(id):
        ps_scanlistfile.write(">" + id + "\n" + seqlib[id])

proteinlistfile.close()
ps_scanlistfile.close()