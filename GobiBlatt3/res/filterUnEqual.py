#! python

import sys

out = open(sys.argv[1], 'w')
inref = open(sys.argv[2], 'r')
indata = open(sys.argv[3], 'r')
out2 = open(sys.argv[4], 'w')

mydata = {}
refdata = {}

for line in indata:
	mydata[line.split(':')[0][:-1]] = line.split(':')[1]
indata.close()

for line in inref:
	refdata[line.split(':')[0]] = line.split(':')[1]
inref.close()

for x in mydata:
	if mydata[x] != refdata[x]:
		out.write(x+" "+x+"D\n")
	else:
		out2.write("1.0\n")
out.close()