from pybrain.datasets            import ClassificationDataSet
from pybrain.tools.shortcuts     import buildNetwork
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.tools.customxml.networkwriter import NetworkWriter
from pybrain.tools.customxml.networkreader import NetworkReader
from pybrain.structure import TanhLayer, SigmoidLayer


import random

def getData():
    fo = open("C:\\Program Files (x86)\\Lux\\Support\\data1per.txt")
    #data = []

    '''
    correctinds = range(0,5)
    for k in range(5, 131, 3):
        correctinds.append(k)
    correctinds.append(129)
    correctinds.append(130)
    for k in range(131, 257, 3):
        correctinds.append(k)
    correctinds.append(255)
    correctinds.append(256)
    '''

    #alldata = ClassificationDataSet(92, 1)
    alldata = ClassificationDataSet(84, 1)

    count = 0
    for line in fo.readlines():
    #for k in range(0, 20000):
        count += 1

        #line = fo.readline()

        line = [int(x.strip()) for x in line[1:-3].split(',')]
        line = [line[0]]+line[4:47]+line[49:90]

        alldata.addSample(line[1:], line[0])
    print count
    return alldata

def trainData(data, filename):

    net = buildNetwork(data.indim, 40, data.outdim, hiddenclass=TanhLayer, outclass=SigmoidLayer)
    trainer = BackpropTrainer( net, dataset=data, verbose=True, momentum=0.1, weightdecay=0.01)
    _ , valid_errors = trainer.trainUntilConvergence(continueEpochs=2)
    NetworkWriter.writeToFile(net, filename)
    print "Valid error: ", min(valid_errors)
    return net

def printConnections(n):
    for mod in n.modules:
        for conn in n.connections[mod]:
            print conn
            for cc in range(len(conn.params)):
                print conn.whichBuffers(cc), conn.params[cc]


data = getData()
#print data['input'][1]
net = trainData(data, 'pybrainrisk5full.xml')
#net = NetworkReader.readFrom('pybrainrisk1full.xml')
#printConnections(net)
#print net.activate(data['input'][0])
#d2 = data['input'][1]
#d2[1] += 1
#print net.activate(d2)
#print net.activate(data['input'][0])
#print data['target'][0]

#for k in range(0, 50):
#    print net.activate(data['input'][k]), data['target'][k]
#print net


'''
92/90/1: pybrainrisk1. Valid error:  0.0345625648592 over 5k, 2 continue
92/1/1: pybrainrisk2. Valid error:  0.033900722208 over 5k, 2 continue
92/90/1: pybrainrisk1. 0.0345126347243 over 5k 2 continue no momentum
92/90/50/1: 0.0320080022035
92/90/50/30/1: 0.0362017309625
92/90/90/1: 0.0324838036673
92/500/100/1: 0.0341315736284

halved data
92/90/1: 0.0299813625887 0.030274245687
92/90/50/1: 0.0343008038243

switched to tanh hiddenclass: 0.0317842601619. pybrainrisk2
switched to tanh outclass: 0.0305174886599. pybrainrisk3
switched to sigmoid outclass: 0.0306065833025. pybrainrisk4
sigmoid hidden and out: 0.0383301489749
back to tanh/sigmoid. 92/90/1: 0.0288832332389. pybrainrisk5
92/1/1 0.0420569127563
84/90/1 ditched aggregates: 0.345
84/90/50/1: 0.035094175973 pybrainrisk6
92/90/50/1 with 1/-1 instead of 1/0: I forget but a bit worse

92/90/1 full data: 0.0307917419771 pybrainrisk1full
92/70/40/1 full data: 0.0308485540349 pybrainrisk2full
92/50/1 full data: 0.0304555368492 pybrainrisk3full
92/40/1 full data: 0.0300968522158 pybrainrisk4full
92/40/1 full data only macro features: 0.0354337732922 pybrainrisk5full
'''