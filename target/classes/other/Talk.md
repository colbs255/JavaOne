---
marp: true
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
backgroundImage: url('https://marp.app/assets/hero-background.svg')
style: |
  .columns {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 1rem;
  }
---
# <!--fit--> Java after 11

---
# Overview
1. Java 17 Features
2. Post Java 17 Features
3. Infrastructure Improvements
4. Fun Stuff

---
# Java 17 Features
- Pattern matching for instanceof
- Records
- Sealed Classes
- Switch Expressions
- Text Blocks
- Modules
- Better NullPointerExceptions
- Garbage Collection Improvements

---
## Pattern Matching for instanceof
<div class="columns"> 

<div class="columns-left">
We've all seen code like this...

```java
Object o = someRandomObject();
// Check type
if (o instanceof String) {
    // Cast to String
    String s = (String)o;
    // do something with String s
} else if (o instanceof Number) {
    // Cast to Number
    Number n = (Number)o;
    // do something with Number n
}
```
</div>

<div class="columns-right">
We can condense this:

```java
Object o = someRandomObject();
// Check type and cast to String
if (o instanceof String s) {
    // do something with String s
// Check type and cast to Number
} else if (o instanceof Number n) {
    // do something with Number n
}
```
</div>

</div>

---
## Another instanceof example
<div class="columns">
<div class="columns-left">
Consider an equals method:

```java
public final boolean equals(Object o) {
    if (!(o instanceof Point)) return false;
    Point other = (Point) o;
    return x == other.x && y == other.y;
}
```
</div>
<div class="columns-right">
We can now write this as:

```java
public final boolean equals(Object o) {
    return (o instanceof Point other)
        && x == other.x && y == other.y;
}
```
</div>
</div>

---
## Records
- Immutable, final, transparent class
- No extend clause but can implement an interface
<div class="columns">
<div class="columns-left">
Consider a simple data class:

```java
final class Range {
    private final int start;
    private final int end;

    Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    int start() { return start; }
    int end() { return end; }

    public boolean equals(Object o) {
        if (!(o instanceof Range)) return false;
        Range other = (Range) o;
        return other.start == start && other.end == end;
    }

    public int hashCode() { return Objects.hash(start, end); }

    public String toString() { return String.format("Range[start=%d, end=%d]", start, end); }
}
```
</div>

<div class="columns-right">
Records can do this in one line:

```java
record Range(int start, int end) { }
```
Usage:
```java
var range = new Range(2, 3);
System.out.println(range.start());
System.out.println(range.end);
```
</div>
</div>

---
## Record Constructors
- Records are automatically given `canonical constructors` that set the instance values (what we used in the previous slide)
- You can make your own, but **all constructors must ultimately call the canonical constructor**
```java
record Range(int start, int end) {
    // Canonical constructor that uses the compact notation
    Range {
        if (end < begin) { throw new IllegalArgumentException("Begin must be less than end"); }
    }

    // Has to use the canonical constructor
    Range(int end) { this(0, end); }
}
```

---
## Better NullPointerExceptions
- An NPE might for this code `a.b.c.i = 99` looks like this:
```text
Exception in thread "main" java.lang.NullPointerException at Prog.main(Prog.java:5)
```
- Which variable was null? a, b, c, or i? The message doesn't tell you
- The error message in new java looks like this:
```text
Exception in thread "main" java.lang.NullPointerException: Cannot read field "c" because "a.b" is null at Prog.main(Prog.java:5)
```

---
## Text Blocks

---
## Sealed Classes

---
## Switch Expressions

---
## Modules

---
## Garbage Collectors
### Shenandoah
### ZGC
### G1

---
# Post Java 17 Features
## Pattern Matching for switch
## Foreign Function & Memory API (Panama)
## Vector API
## Virtual Threads
## Structured Concurrency
## Reflection uses Method handles
## Record Patterns

# Infrastructure
## GraalVM
## CRaC
## Micrometer

# Fun Stuff
- Which of these are valid?
    - toList(), collect(toList())
- Generics were introduced in bytecode in 1.3
- goto: is a keyword but you can't use it
- you can name something var var (is var a restricted identifier)
- enums have limits to how many
- bytes are represented as ints
- Sorting/compare error
- Regular Expressions Error