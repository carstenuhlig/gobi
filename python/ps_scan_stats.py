#!/usr/bin/python
import sys

__author__ = 'carsten'

results = open(sys.argv[1], 'r')
stats = open(sys.argv[2], 'w')

data = {}

for line in results:
    if line.startswith(">"):
        pieces = line.split(" ")
        # ohne abfrage testen ob bla direkt data[bla] direkt initialisiert wird
        kkey = pieces[3]
        if kkey in data:
            data[kkey] += 1
        else:
            data[kkey] = 0
            print kkey

results.close()

for key in data:
    stats.write(key + "\t" + str(data[key]) + "\n")

stats.close()
