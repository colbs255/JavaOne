# Quick Notes

Java first
Writing bad code is super costly in the long term
Java is now released every 6 months
Projects
Amber - more approachable, reduce overhead
Leyden - reduce overhead
Loom - virtual thefts
Panama - java programs with non java components (c/c++)
Valhalla - value types
ZGC - scalable low latency garbage collector

Java 8 vs 17 - 64%
Java SE Subscription enterprise performance pack - java 17 performance for java 8 code/requirements

GraalVm is basically merging with openjdk - releases will align

Java Champion / JUG

What is PQC?
Join a JUG
Panama stuff
Devoxx

Readable code one

Official java podcast
Bookmark both of these:
inside.java
dev.java

methods that are hot spots (hence the name of the OpenJDK JVM) and compiles them to get better performance than interpreting the bytecodes.  This takes place in two phases.  Initially, the C1 compiler is used, which is a fast compiler without extensive optimization.  The C2 compiler is subsequently used for very hot methods, which uses profiling data collected from the running application to optimize as much as possible.  Techniques like aggressive method inlining and speculative optimizations can easily lead to better performing code than generated ahead of time (AOT) using a static compiler

In fact, in very early Java versions, the JVM threads were multiplexed onto OS threads (also known as platform threads), in what were referred to as green threads because those earliest JVM implementations actually used only a single platform thread.

However, this single platform thread practice died away around the Java 1.2 and Java 1.3 era (and slightly earlier on Sun’s Solaris OS). Modern Java versions running on mainstream OSs instead implement the rule that one Java thread equals exactly one OS thread

Programmers are forced to choose between modeling a unit of domain concurrency directly as a thread and lose considerably in the scale of concurrency a single server can support, or use other constructs to implement concurrency on a finer-grained level than threads (tasks), and support concurrency by writing asynchronous code that does not block the thread running it

Ideas:
Go through doc in IntelliJ, or make wiki
Ask question at beginning for motivation
For threads,
Give them the quick reason first, then dive into the details
Trivia crack

Avoid multi line lambdas
Hard to test
Hard to read
Leads to code duplicate
Noisy


Spock

burk.hufnagel@daugherty.com