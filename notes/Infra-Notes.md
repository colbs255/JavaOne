# Infra

# CRaC
- way to pause and restore a JVM based app
- doesn't require a closed world
- improved throughput from start
- openjdk project
- allows faster startup - less initial cpu resources
- it's sort of an alternative to native images

# IntelliJ
- Task Switcher: Ctrl + Tab

# GraalVM
AOT vs JIT
Faster than go
Building native images takes a ton of memory - many pipelines can’t support it. GitHub actions now can
JIT and GraalVM have escape analysis
An executable file produced by Native Image has several important advantages, in that it:
-jar : Specify the location of the JAR file containing the Java application. (You can also specify the classpath with -cp.)
--no-fallback: Do not generate a fallback image. (A fallback image requires a JVM to run it, and you do not need this.)
-H:Class: Specify the class that provides the entry point method (the main method).
-H:Name: Specify the name of the output executable file.

Uses a fraction of the resources required by the JVM, so is cheaper to run
Starts in milliseconds
Delivers peak performance immediately, with no warmup
Can be packaged into a lightweight container image for faster and more efficient deployment
Presents a reduced attack surface (more on this in future labs)
Many of the leading microservice frameworks support ahead-of-time compilation with GraalVM Native Image, including Micronaut, Spring, Helidon, and Quarkus.

# Micrometer
3 Pillars - Metrics, logging, distributed tracing
## Metrics
- Book by John Schneider for metrics
- Efficient and cheap to store
- CONSISTENT data collection - regular intervals
- Works with the "knowns", canary in a coal mine
- Sum / count = aggregable average, can't do percentiles
- OTLP: micrometer can send metrics to OTLP collector, they can work together
- Micrometer tracing (just the spring tracing lib)
- Docs generator for metrics
- github: https://github.com/micrometer-metrics/micrometer-samples
- Micrometer provides a simple facade over the instrumentation clients for the most popular monitoring systems. Think SLF4J, but for application metrics! Application metrics recorded by Micrometer are intended to be used to observe, alert, and react to the current/recent operational state of your environment
- SpringOne Dec 6-8

# Error Prone and Refaster

# Microservices
Think about the result not the tech
Don’t just use the latest
Microservices - increased agility at cost of complexity. Don’t just make it the default option
Micro is good for stateless stuff. Breaking up a transactional app is very difficult

Mono
Easy deployment
Easy testing and debugging
One code base

Micro
Expensive
High complexity
Overkill
Remote calls more expensive
Debugging is hard
DR aid difficult