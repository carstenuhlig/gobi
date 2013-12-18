#! python

import sys

reader = open(sys.argv[1], 'r')
writer = open(sys.argv[2], 'w')

def calcIdentity(stringa,stringb):
	counter = 0
	counter2 = 0
	if len(stringa) != len(stringb):
		return 0
	for x in range(len(stringa)):
		if stringa[x] == stringb[x]:
			#print stringa[x]+stringb[x]
			counter += 1
		counter2 += 1
	return float(counter)/len(stringa)


b = ""

for line in reader:
	if not line[:1] == ">":
		if not b == "":
			if len(line.split(': ')) < 2:
				print line
			s = calcIdentity(b,line.split(': ')[1])
			writer.write(str(s) + " " + line.split(': ')[0][:-1] + "\n")
		else:
			b = line.split(': ')[1]
	else:
		b = ""

reader.close()
writer.close()