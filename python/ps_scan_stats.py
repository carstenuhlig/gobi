#!/usr/bin/python
import sys

__author__ = 'carsten'

results = open(sys.argv[1], 'r')
stats = open(sys.argv[2], 'w')

data = {}

for line in results:
    if line.startswith(">"):
        pieces = line.split(" ")
        bla = ""
        for i in range(4, pieces.length()):
            bla += pieces[i]
        if data.has_key(bla): # ohne abfrage testen ob bla direkt data[bla] direkt initialisiert wird
            data[bla] += 1

results.close()

for key in data:
    stats.write(data[key] + "\n")

stats.close()