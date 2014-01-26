#! python

str = raw_input("Pairfile: ")

pair = open(str, "r")

str = raw_input("Pairfile zum schreiben: ")

pairwrite = open(str, "w")

for line in pair:
	a = line.split(':')[0]
	b = line.split(':')[1]
	pairwrite.write(a+" "+b)
pair.close()
pairwrite.close()
