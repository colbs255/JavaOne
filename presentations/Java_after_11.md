---
marp: true
title: Java after 11
theme: uncover
# Dark mode
# class: invert
style: |
    h1,h2 {
        color: #00A2FF
    }
---
# <!--fit--> Java after 11

---
# What's new?
- Better NullPointerExceptions
- Garbage Collection Improvements
- Text Blocks
- Pattern matching for instanceof
- Switch Expressions
- Records
- Sealed Classes
- And more!

---
# Better NullPointerExceptions
```java
a.b.c.i = 99; // Throws a NullPointerException
```
Before...
```text
Exception in thread "main" java.lang.NullPointerException at Prog.main(Prog.java:5)
```
After...
```text
Exception in thread "main" java.lang.NullPointerException: Cannot read field "c" because "a.b" is null ...
```

---
# Garbage Collection (GC)

---
# GC Tradeoffs
- Throughput
    - How much time is spent doing actual application work vs GC work?
- Latency
    - How responsive is it? How does GC affect any single app operation?
- Footprint
    - What additional resources does GC require?

---
# Java GCs
- Serial: optimized for footprint, simple, single threaded
- Parallel: optimzed for throughput
- G1 (Garbage First): balance of latency and throughput
- ZGC (Z Garbage collector): optimzed for latency
    - Low latency

---
# GC Benchmarks
|  |  |
|------------|---------|
| ![w:540](https://kstefanj.github.io/assets/posts/gc-8-17/throughput.png)| ![w:540](https://kstefanj.github.io/assets/posts/gc-8-17/latency.png)|


- [Stefan Johansson - GC progress from JDK 8 to JDK 17](https://kstefanj.github.io/2021/11/24/gc-progress-8-17.html)

---
# Text Blocks
- Multi-line string **literal**
- Doesn't need escape sequences (generally)
```java
String grossJson = "{\n\"id\": 1,\n\"qty\": 5,\n\"price\": 100.00}";
String prettyJson = """
            {
                "id": 1,
                "qty": 5,
                "price": 100
            }
            """;
```

---
- Indentation determined by farthest left character & closing quotes
- Single line blocks are supported with `\`:
```java
String text = """
              Lorem ipsum dolor sit amet, consectetur adipiscing \
              elit, sed do eiusmod tempor incididunt ut labore \
              et dolore magna aliqua.
              """;
```

---
# Pattern Matching for instanceof

---
Before...
```java
Object o = someRandomObject();
if (o instanceof String) {
    String s = (String)o;
    // do something with String s...
} else if (o instanceof Number) {
    Number n = (Number)o;
    // do something with Number n...
}
```

---
After...
```java
Object o = someRandomObject();
if (o instanceof String s) {
    // do something with String s...
} else if (o instanceof Number n) {
    // do something with Number n...
}
```

---
Before...
```java
public final boolean equals(Object o) {
    if (!(o instanceof Point)) return false;
    Point other = (Point) o;
    return x == other.x && y == other.y;
}
```

After...
```java
public final boolean equals(Object o) {
    return (o instanceof Point other)
        && x == other.x && y == other.y;
}
```

---
# Switch Expressions

---
Before...
```java
int numLetters; // gross
switch (day) {
    case MONDAY:
    case FRIDAY:
    case SUNDAY:
        numLetters = 6;
        break;
    case TUESDAY:
        numLetters = 7;
        break;
    // Thursday, Saturday, Wednesday...
}
```

---
After...
```java
int numLetters = switch (day) {
    // Arrows means no breaks needed, they don't "fall through"
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY                -> 7;
    case THURSDAY, SATURDAY     -> 8;
    case WEDNESDAY              -> 9;
}
```
- Expression returns a value
- Must be exhaustive, but 'default' is not required

---
# Records

---
Before...
```java
final class Range {
    private final int start;
    private final int end;

    Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int start() { return start; }
    public int end() { return end; }
    public boolean equals(Object o) { /*...*/ }
    public int hashCode() { /*...*/ }
    public String toString() { /*...*/ }
}
```

---
After...
```java
record Range(int start, int end) { }
```

Usage:
```java
var range = new Range(2, 3);
System.out.println(range.start());
System.out.println(range.end);
```

---
# Record Properties
- Immutable
- Transparent
- Can't extend any class (implicitly extends record)
- Can't be extended
- But can implement interfaces

---
# Record Constructors
- Automatically given `canonical constructors`
- You can make your own, but **all constructors must ultimately call the canonical constructor**
```java
record Range(int start, int end) {
    // Canonical constructor that uses the compact syntax
    Range {
        if (end < start) { throw new IllegalArgumentException("start must be less than end"); }
    }

    // Has to use the canonical constructor
    Range(int end) { this(0, end); }
}
```

---
# Sealed Classes
```java
class Shape { } // No limits to extension
```
```java
final class Shape { } // Nothing can extend
```
- A sealed class can only be extended by classes **permitted** to do so
```java
sealed class Shape {
    permits Circle, Rectangle, Triangle {
} 
class Circle extends Shape {}
class Rectangle extends Shape {}
class Triangle extends Shape {}
```

---
# Data Oriented Programming

---
- Combine it all together and you get Data Oriented Programming
```java
sealed interface AsyncReturn<V> {
    record Success<V>(V result) implements AsyncReturn<V> { }
    record Failure<V>(Throwable cause) implements AsyncReturn<V> { }
    record Timeout<V>() implements AsyncReturn<V> { }
    record Interrupted<V>() implements AsyncReturn<V> { }
}
```

```java
AsyncResult<V> r = future.get();
switch (r) {
    case Success<V>(var result): ...
    case Failure<V>(Throwable cause): ...
    case Timeout<V>(): ...
    case Interrupted<V>(): ...
}
```
- [Brian Goetz - Data Oriented Programming in Java](https://www.infoq.com/articles/data-oriented-programming-java)

---
# Fun Stuff

---
# Stream::toList
<!--
    Simply creating a list of ints
    The new toList method makes this more fluid
    I know, I know.. it is just a few characters... but it is hard to go back once you have used it
-->
Before...
```java
var nums = IntStream.range(0, 10)
                    .boxed()
                    .collect(Collectors.toList());
```

After...
```java
var nums = IntStream.range(0, 10)
                    .boxed()
                    .toList();
```

---
# Do these compile?
```java
int x = 1;

int class = 1;

int goto = 1;

int static = 1;

int var = 1;

int void = 1;

int const = 1;
```

---
# Solution
```java
int x = 1;          // Yes...

int class = 1;      // No, java keyword

int goto = 1;       // No, java keyword that is not actually used (reserved)

int static = 1;     // No, java keyword

int var = 1;        // Yes!

var var = "var";    // Yes!

int void = 1;       // No, java keyword

int const = 1;      // No, another reserved java keyword
```

---
# Comparison Method Violates its General Contract!
- Why? Your comparator has a bug. Sort call might throw this exception if it notices the failure
- Comparator rules
    - must impose a total ordering of values (ordering relation valid for all pairs)
- Error 1: int overflow with subtraction
- Error 2: autounboxing (with equals), avoid equals and not equals in value comparison
- Just use Integer::compare
- Even if you do it correctly, it won't work with floating point numbers due to NaN (not a number)
- null first should not be wrapped around the comparator
    - have to put nulls first inside
- don't write your own comparator, combine things together
- https://www.youtube.com/watch?v=Enwbh6wpnYs

---
# Conclusion
Java 17...
- Makes it easier to debug programs with more precise null pointer exceptions
- Improves overall performance with it's enhanced garbage collectors
- Makes writing java more convenient with text blocks, pattern matching, switch expressions, and records
- Sealed classes
