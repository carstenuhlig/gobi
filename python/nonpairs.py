#! python

ref_file = open('reference_sequences.txt','r')

# hashmaps init
pairs = {}
ref = {}

for line in ref_file:
#	print line.split(':')[0]
	pairs[line.split(':')[0]] = ""
ref_file.close()

seqlib_file = open('seqlibfile.txt','r')
pairs_file_to_write = open('pairfile.txt','w')

for line in seqlib_file:
  	if (line.split(':')[0])[:-1] in pairs:
		a = line.split(':')[0][:-1]
		pairs_file_to_write.write(a+':'+a+'D\n')
seqlib_file.close()
pairs_file_to_write.close()
