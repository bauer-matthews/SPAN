import argparse
import subprocess
import os

from subprocess import Popen, PIPE

parser = argparse.ArgumentParser()
parser.add_argument("--test_files_dir")
parser.add_argument("--jar_location")
parser.add_argument("--maude")
parser.add_argument("--kiss")
parser.add_argument("--akiss")
parser.add_argument("--glpk")
parser.add_argument("--equiv")
parser.add_argument("--equiv_engine")
args = parser.parse_args()

outFile = open('./results.csv', 'w+')

if args.equiv == "no":
    print >> outFile, "test name, recipe size, time, attack found, maximum attack prob, belief states, states"
else:
    print >> outFile, "test name, recipe size, time, attack found, belief states, states"

for root, dirs, filenames in os.walk(args.test_files_dir):

    for f in filenames:

        testName = root[root.rfind('/') + 1:]
        tail = f[f.rfind('_')+1:]
        recipeSize = tail[0:tail.rfind('.')]

        print("starting: " + testName + " " + recipeSize)

        if args.equiv_engine == "kiss":
            p = Popen(['java', '-jar', args.jar_location, "-maxAttack", "-kiss", args.kiss, "-maude", args.maude, '-protocol', os.path.join(root,f)], stdin=PIPE, stdout=PIPE, stderr=PIPE)
        else:
            p =   p = Popen(['java', '-jar', args.jar_location, "-maxAttack", "-akiss", args.akiss, "-maude", args.maude, '-protocol', os.path.join(root,f)], stdin=PIPE, stdout=PIPE, stderr=PIPE)

        output, err = p.communicate();

        if not err:
            print("completed: " + testName + " " + recipeSize)
        else:
            print("error: " + testName + " " + recipeSize + " - failed!")
            print(err)


        time = ''
        prob = ''
        beliefs = ''
        states = ''
        found = ''

        for item in output.split("\n"):

            if "Running time" in item:
                timeLine = item.strip()
                time = timeLine[timeLine.rfind(':') + 1:timeLine.rfind('m')].strip(' \t\n\r')

            if "probability" in item:
                probLine = item.strip();
                prob = probLine[probLine.rfind(':')+1:].strip(' \t\n\r')

            if "Belief" in item:
                beliefsLine = item.strip();
                beliefs = beliefsLine[beliefsLine.rfind(':')+1:].strip(' \t\n\r')

            if "States" in item:
                statesLine = item.strip();
                states = statesLine[statesLine.rfind(':')+1:].strip(' \t\n\r')

            if "Attack found" in item:
                found = "yes"

            if "No attack found" in item:
                found = "no"

        print >> outFile, testName + ", " + recipeSize + ", " + time + ", " + found + ", " + prob + ", " + beliefs + ", " + states

exit(0)


