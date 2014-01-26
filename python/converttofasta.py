#!/usr/bin/python

import sys

rawfile = open(sys.argv[1], 'r')
processedfile = open(sys.argv[2], 'w')

for line in rawfile:
    name = line.split(':')[0]
    seq = line.split(':')[1]
    processedfile.write(">" + name + "\n" + seq + "\n")
rawfile.close()
processedfile.close()
