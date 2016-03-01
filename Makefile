JC       = javac
JFLAGS   = -g
CLASSES  = NaiveBayesClassifier.class Feature.class TimeWatch.class

.SUFFIXES: .java .class

default: $(CLASSES)

.java.class:
	$(JC) $(JFLAGS) $<

clean:
	rm -f *.class
