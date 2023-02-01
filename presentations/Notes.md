So this talk is going to focus on the new features after Java 11

# What's new?
More specifically, things like this:

---
# Better NullPointerExceptions
- Let's start with NPEs
- Pretend we get an exception like this in prod
- In java 11, we would get an exception like this
- we don't know where it occurs
- now we do, we see
- this happens a lot in prod

---
# Garbage Collection (GC)
Let't talk about GC improvements

---
# GC Trade-offs
- but before I do that, I want to set the stage a bit
- garbage collectors are all about tradeoffs
- Think about XCLR
- Think about the UI service or video games

---
# Java GCs
- Serial: let's app run like normal then pauses and collects
- Parallel: like serial but uses multiple threads for collecting
- These are older collectors
- Now we have new ones:
    - G1 (default): what TMS uses
    - Shenandoah: latency
    - ZGC: latency
    - why 2 latency collectors? Well, Oracle owns ZGC redhat owns Shenandoah
    - Epsilon: lastly, we have my favorite collector. Pretty revolutionary, it frees memory, and then does nothing. Effectivly turns off garbage collection
- Why did I just list all this out? To show you we have options - we can tune things, and they affect more than just the memory your app uses

---
# GC Benchmarks
|  |  |
|------------|---------|
| ![w:540](https://kstefanj.github.io/assets/posts/gc-8-17/throughput.png)| ![w:540](https://kstefanj.github.io/assets/posts/gc-8-17/latency.png)|


- [Stefan Johansson - GC progress from JDK 8 to JDK 17](https://kstefanj.github.io/2021/11/24/gc-progress-8-17.html)

---
# Text Blocks
- Now we get into actual java language features like text blocks

---
Before...
```java
String grossJson = "{\n\"id\": 1,\n\"qty\": 5,\n\"price\": 100.00}";
```

---
After...
```java
String prettyJson = """
            {
                "id": 1,
                "qty": 5,
                "price": 100
            }
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
- Can implement interfaces

---
# Record Constructors
- Automatically given `canonical constructors`
- **All** constructors must ultimately call the canonical constructor
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
class Circle extends Shape { }
class Rectangle extends Shape { }
class Triangle extends Shape { }
```

---
# What happens when we combine these?
- Pattern Matching
- Switch Expressions
- Records
- Sealed Classes

---
# Data Oriented Programming
```java
sealed interface AsyncResult<V> {
    record Success<V>(V result) implements AsyncResult<V> { }
    record Failure<V>(Throwable cause) implements AsyncResult<V> { }
    record Timeout<V>() implements AsyncResult<V> { }
    record Interrupted<V>() implements AsyncResult<V> { }
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

int var = 1;        // Yes! Reserved type name, not a keyword!

int void = 1;       // No, java keyword

int const = 1;      // No, another reserved java keyword

var var = "var";    // Yes!
```

---
# Comparison Method Violates its General Contract!
- Based on [talk by Stuart Marks](https://www.youtube.com/watch?v=Enwbh6wpnYs&t=3s)

---
# Does this work?
```java
List<Integer> numbers = ...
Comparator<Integer> comparator = (a,b) -> a - b;
numbers.sort(comparator);
```

---
# No!
- Example
    - a: large positive
    - b: large negative
- (a - b) overflows, creating a negative number
- Since (a - b) is negative, comparator thinks a < b

---
# Does this work??
```java
List<Integer> numbers = ...
Comparator<Integer> comparator = (a,b) -> a < b
    ? -1
    : a == b ? 0 : 1;
numbers.sort(comparator);
```

---
# No!
- Auto-unboxing is the problem!
- the a == b is performing reference equality
- so a == b is always false
    - Unless a and b are the same object

---
# Does this work???
```java
List<Double> numbers = ...
Comparator<Double> comparator = (a,b) -> a < b
    ? -1
    : a > b ? 1 : 0
```

---
# No!
- Example:
    - a: NaN
    - b: any number
- ANY comparison with NaN evaluates to false
```java
NaN < 1000 -> false
NaN > 1000 -> false
NaN == 1000 -> false
```

---
# ...does this work????
```java
List<Integer> numbers = ...
numbers.sort(Integer::compare);
```

---
# Yes!
- Lesson: Just use `Integer::compare`

---
<style scoped>
ul {
  font-size: 35px;
}
</style>
# Conclusion
- Java 17 improves...
    - System Performance
        - Enhanced garbage collectors
    - Developer Velocity
        - Better null pointer exceptions
        - Text blocks, Stream::toList
        - Pattern matching, switch expressions, and records
    - Developer Flexibility
        - Sealed classes
        - Data Oriented Programming ([Brian Goetz Article](https://www.infoq.com/articles/data-oriented-programming-java))
