import argparse
import subprocess
import os

from subprocess import Popen, PIPE

parser = argparse.ArgumentParser()
parser.add_argument("test_files_dir")
parser.add_argument("jar_location")
args = parser.parse_args()

outFile = open('./results.csv', 'w+')

print >> outFile, "test name, recipe size, time, attack found, attack prob, paths explored"

for root, dirs, filenames in os.walk(args.test_files_dir):

    for f in filenames:

        p = Popen(['java', '-jar', args.jar_location, '-protocol', os.path.join(root,f)], stdin=PIPE, stdout=PIPE, stderr=PIPE)
        output, err = p.communicate();

        testName = root[root.rfind('/') + 1:]
        tail = f[f.rfind('_')+1:]
        recipeSize = tail[0:tail.rfind('.')]

        print(testName + " " + recipeSize)

        time = ''
        prob = ''
        paths = ''
        found = ''

        for item in output.split("\n"):

            if "Running time" in item:
                timeLine = item.strip()
                time = timeLine[timeLine.rfind(':') + 1:timeLine.rfind('m')].strip(' \t\n\r')

            if "probability" in item:
                probLine = item.strip();
                prob = probLine[probLine.rfind(':')+1:].strip(' \t\n\r')

            if "Paths" in item:
                pathsLine = item.strip();
                paths = pathsLine[pathsLine.rfind(':')+1:].strip(' \t\n\r')

            if "Attack found" in item:
                found = "yes"

            if "No attack found" in item:
                found = "no"

        print >> outFile, testName + ", " + recipeSize + ", " + time + ", " + found + ", " + prob + ", " + paths

exit(0)


