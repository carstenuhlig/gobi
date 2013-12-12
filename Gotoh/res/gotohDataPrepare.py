#! python

import sys

reader = open('reference_sequences.txt','r')
writer_p = open(sys.argv[1],'w')
writer_s = open(sys.argv[2],'w')

for line in reader:
	anid = line.split(':')[0]
	seq = line.split(':')[1]
	writer_p.write(anid+'2 '+anid+"\n")
	writer_s.write(anid+'2:'+seq)
	writer_s.write(anid+':'+seq)

reader.close()
writer_p.close()
writer_s.close()