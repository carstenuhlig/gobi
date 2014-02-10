#!/usr/bin/python

import sys
import re

regex = r'^\S+\s+\S+\s+\S+\s+CDS\s+\d+\s+\d+\s+\S+\s+\S+\s+\d\s+gene_id\s+"(ENSG\d+)"'

with open(sys.argv[1], 'rt') as f:
    for line in f:
        match = re.search(line, regex)
        if match:
        	print line