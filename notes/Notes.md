# Pattern Matching
- If it succeeds, the variable will be initialized
- Can be used to make equals methods simpler
  - Effective Java: You can clean up the equals with
```java
public final class CaseInsensitiveString {
    private final String s;
    @Override
    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString
                && ((CaseInsensitiveString )o).s.equalsIgnoreCase(s);
    }
    
    public boolean betterEquals(Object o) {
        return o instanceof CaseInsensitiveString other
                && other.s.equalsIgnoreCase(s);
    }
}
```
## Where can you use the pattern variable?
Flow Sensitive Scoping
```java
class Example {
  {
    Object o = new Object();
    if (o instanceof String s) {
        System.out.println(s);
    } else {
        // can't use s here
    }
    
    if (!(o instanceof String s)) {
        // can't use here
    } else {
        System.out.println(s);
    }
  }
}
```
## Switch Statements
- Only 1 gets evaluated
- If 1 dominates the other, (object before string), you will get a compiler error
## Pattern Matching with records
- You can down-cast automatically here within the switch!!

```java
import java.time.LocalDate;

record Pair(Object a, Object b) {
  static void example() {
    if (o instanceof Pair(String s,String t)) {
      // Check it's a pair
      // check s is a string
      // check t is s atring

    } else if (o instanceof Pair(Object a,Object b)) {

    }
  }

  static void crazyExample() {
    Object o = new Pair(new Pair("Java", 1), LocalDate.of(2022, 10, 18));
    if (o instanceof Pair(Pair a, LocalDate b)) {
        if (a instanceof Pair(String x, int y)) {
            // do nothing
        }
    }
    
    // Short form
    // This is amazing, it's basically a query
    if (o instanceof Pair(Pair(var x, var y), var b)) {
        
    }
  }
}
```
- Can also do the var short hanlers


# Records
https://dev.java/learn/using-record-to-model-immutable-data/
Note that a compact constructor not only doesn't require you to assign fields, it prevents you from doing so - that's something that falls to the generated code. So if you need to assign a different value to the field than the argument, you can't do that directly. Instead, you need to reassign the desired value to the parameter.

Just like classes, records can have additional constructors but unlike in classes, they must start with a this(...) call to another constructor. Since recursive constructor calls are forbidden, this means all constructor calls eventually end with the canonical constructor. So it's good practice to put all verification or mutation of arguments into the canonical constructor and then call that from other constructors.
For some types, it is preferable to allow construction through static factory methods instead of constructors. Records allow that as well, but the common practice to then make the constructor(s) private does not work. Records are transparent carriers (of immutable data), and that transparency requires their full API to be accessible - that includes the means to construct them from their constituent parts. A possible compromise is to create static factory methods and then mark the constructor as deprecated to discourage its use.

A possible exception is a record that has a mutable collection as field (which is in itself questionable because, ideally, a record is immutable) but wants to return an unmodifiable view on it.

# Text Blocks
Must have new line after triple quotes
the closing block controls the indentation
Consequently, text blocks strip white space, so any tab, space, or other whitespace characters that may appear in a line of a text block before the newline, will be removed.
If it is indeed important to keep white space at the end of a line, you can insert the new escape sequence \s. It translates to a single space (\u0020) and everything preceding it is conserved - even other white space. So in the following example, each line is exactly six characters long:
https://openjdk.org/jeps/378

# I didn't know that about java
- Generics were introduced in bytecode in 1.3
- goto: is a keyword but you can't use it
- you can name something var var
- enums have limits to how many
- bytes are represented as ints
# TDD
- RED(fail) -> GREEN(pass) -> BLUE(refactor)
  - refactoring includes refactoring the tests... not just the code
- Tools
  - CodeClimate, SilverThread, CodeScene, Refactor First
  - https://approvaltests.com
    - 
# Data Oriented Programming
- Model data as immutable data and keep the code that acts on it separate
- NOT a replacement for oop, it fits well with OOP
- OOP is great with boundaries, but apps are becoming microservices... less need for these boundaries
- We can change the future interface (which throws a bunch of stuff)
  - Timeout, Success, Failure, Interrupted, etc
  - instead of exceptions
  - Can be used in a tree (Tree -> {nil or Node})
    - inorder and postorder traversal
  - JSON (JSONValue: string, int, null, boolean, etc)
    - int, string, etc
## Records (transparent data carriers)
## Sealed Classes


# Sortinng/Compare Error
- Java sort MAY throw an IllegalArgumentException if it detects a Comparable that violates the Comparable contract
  - not required to, but may
- Your comparator needs to enforce total ordering (from math: transitiviy, antisymmetry, substitubility, reflexivity)
## Working with the Dynamic Features of Java
This exception is caused by the addition of the Log4J library because it relies on reflection.
The native-image tool performs an aggressive static analysis to see which classes are used within the application. For any classes not used, the tool will assume that they are not needed. This is called the "closed world" assumption - everything that needs to be loaded must be known when building an executable file. If it is not findable by static analysis, then it will not be included in the executable file.
Reflection is a core feature of Java, so how can you use reflection and take advantage of the speed-ups offered by GraalVM Native Image? You need a way to let the native-image tool know about any uses of reflection.
Luckily, the native-image tool is able to read in configuration files that specify all classes that are referenced through reflection.
You can do this manually, or the Java tracing agent that comes with the GraalVM Java runtime can do this for you. The agent generates JSON files that record all instances of reflection, JNI, proxies, and resources access that it can locate while your application is running.
Note : It is important to exercise all the code paths in your application when running the tracing agent in order to ensure that all cases of reflection are identified.

In addition, there are Maven and Gradle plugins for Native Image so you can easily build, test, and run Java applications as executable files.
# Loom
- Parallelism - speed up task by splitting into cooperating subtasks scheduled on multiple computing reoucre
- Concurrency
- Little's Law: Concurrency(aka # threads) = (throughput) * (latency/duration)
  - more threads means higher throughput, loom allows more threads so more output
- Simpler than reactive streams but still get the concurrency benefits
- Async programming - severs the request/task from the thread
  - hard to debug, how do you observe? Debuggers don't work
- Virtual thread stacks grow and expand
  - Its stack is copied to the heap memory, and its previous carrier thread becomes free to mount another virtual thread. With this system, platform threads never block.
- virtual threads are useful because you can create many of them, if you're not making many then it won't be useful
- Don't cache expensive objects in thread locals
- Avoid pinning - if it happens a lot and for a long time, it will slow things down
  - Pinning means a virtual thread is pinned to a platform thread
  - use lock object instead of snychronized (wait/notify), it doesn't work with virtual threads yet
    - Something to do with the c code that virtual threads use
    - 
  - this rarely happens, use -Djdk.tracePinnedThreads to detect first
## Virtual Thread Good Practices
- use simple, blocking I/O
- don't use shared thread pools, use new thread per task
- Never pool threads, use semaphores to limit if needed
# Regular Expressions
- StackOverflow and cloudfare went down once due to a regex issue
- ReDos - regex denial of service
- Backtracking: a subexpression may fully match the input (.*), but you want a more specific match, the engine will go back
- Java implementation of regex is backtracking
## Avoid bad backtracking
- use non backtracking regex engine
- Use possessive quantifiers (++, *+)
  - Greedy match as much as possible (don't backtrack)
  - non-greedy: atch as littlte as possible
  - caution with subexpressions
  - Use character classes
    - `[\w_]` instead of `(\w|_)`
```regexp
@.+\.com
```
- question mark makes it match in opposite direction (more of a forward tracking approach)
# Security/Serialization
- Java allows you to block certain classes from being deserialized (blacklist and whitelist)

# Modules
## Why?
- Make dependencies more explicit - you specify your dependencies
- Can use jlink to create small images
- Allows you to define which packages are public - defines APIs
    - This way you won't expose your private modules
    - Makes it hard to use internal APIs - you have to explicitly say you are using them
- New abstraction - the subsystem
- High level documentation
- Evolves with your code - living representation of a projects architecture
## How?
- you can do it incrementally
- Unnamed module
    - all jars on classpath are here
    - can read all modules
    - exports all packages
- Automatic module
- Don't worry about modularizing 3rd party code that isn't modularized... just put it on the classpath and move on

# Java on GPU
- Grace Hopper
- The difficult part of GPUs is you have to move the memory to the gpu, which is way more expensive than CPU
- Most of the work done today is for allowing direct access
- Panama replaces JNI - allowing you to do native operations in pure java code rather than jni/c code
- Remember, this is different from the vector API which is for CPU work

# Panama
To easily and securely enable interoperability between Java and the native world, Panama offers the Foreign Function Memory API. In addition, Panama also delivers the Vector API to enable SIMD programming from plain Java code. This API allows Java developers to express vector computations that will reliably compile at runtime to optimal vector hardware instructions on supported CPU architectures and thus achieve superior performance to equivalent scalar computations
At a high level, the steps required to invoke, from Java code, a foreign function are
Find the target foreign function (in a C library)
To find the target foreign function, the FFM API uses a Linker. A Linker is a bridge between two binary interfaces: the Java Virtual Machine and an Application Binary Interface (ABI), ex. the C ABI.
Allocate foreign memory
The Java client code needs to allocate, using the FFM API, the foreign memory, i.e. off-heap memory, required for proper operation of the native function.
Obtain a method handle of the target foreign function, and invoke it
To perform a downcall (Java code calling native code), the FFM API needs to know which native function to invoke. This is achieved with a FunctionDescriptor which describes the signature of the C function to invoke, i.e. its parameter types and its return type. To model those C types from Java, the FFM API uses various MemoryLayout objects.
The native function will be invoked via a corresponding Method Handle. This Method Handle is created based on the corresponding FunctionDescriptor.
💡 The section above describes, at a high-level, how a downcall (Java code calling native code) is performed but the Linker interoperates in both-direction and also enables upcall (native code calling Java code). Upcalls will be discussed in a later section.
Another choice that we made is to use C for the foreign angle of this workshop. It is important to mention that the FFM API interoperates with any languages that offer an Application Binary Interface (ABI) such as Python, Golang, Rust, Swift, etc.
## JExtract
Jextract is a tool, developed under the Project Panama umbrella, whose goal is to mechanically generates, from the corresponding C-header files, Java bindings for native libraries.
Jextract uses libclang to parse C header files and extract entities from native symbols (struct, typedef, macro, global constant, and function).
Although jextract is developed in the OpenJDK Code Tools project, it is not per se part of the JDK itself. This is due to several reasons. First, jextract isn't yet finished and is only available, at this stage, via Early-Access builds. Second, not all Java developers write code that needs to interoperate with native code. Third, and probably the main reason, jextract would significantly increase the size of the JDK. For all those reasons, jextract is a separate bundle that needs to be downloaded independently.
Can be used to javaextract to do what we did in the tutorial...
javaextract --source -t clang.stdlib -I /usr/include --output src/main/java --include-function atoi /usr/include/stdlib.h

💡 This workshop uses the jextract Early-Access build published in July. While waiting for the next early-access build, you can always build jextract yourself using the instructions at https://github.com/openjdk/jextract.

Installing jextract is straight forward.

Get the jextract Early Access build.
Head to https://jdk.java.net/jextract/ and download the Linux / x64 Early-Access builds using wget.

Copy
> wget https://download.java.net/java/early_access/jextract/2/openjdk-19-jextract+2-3_linux-x64_bin.tar.gz -P ~/

Resolving download.java.net (download.java.net)... 23.55.248.91
Connecting to download.java.net (download.java.net)|23.55.248.91|:443... connected.
HTTP request sent, awaiting response... 200 OK
Length: 153788298 (147M) [application/x-gzip]
Saving to: ‘/home/david_dela/openjdk-19-jextract+2-3_linux-x64_bin.tar.gz’

100%[==========================================>] 153,788,298  112MB/s in 1.3s

‘/home/david_dela/openjdk-19-jextract+2-3_linux-x64_bin.tar.gz’ saved [153788298/153788298]
Untar the archive
Copy
> tar xvf ~/openjdk-19-jextract+2-3_linux-x64_bin.tar.gz
Make sure to update the path to include jextract.

Copy
> export PATH=$PATH:~/jextract-19/bin
💡 If you want the updated path to be persisted, update your .bashrc as follow.

Copy
> echo 'export PATH=$PATH:~/jextract-19/bin' >> ~/.bashrc
Check the insallation
Confirm that jextract is installed properly.

Copy
> jextract --version

jextract 19
JDK version 19-ea+23-1706
WARNING: A restricted method in java.lang.foreign.Linker has been called
WARNING: java.lang.foreign.Linker::nativeLinker has been called by module org.openjdk.jextract
WARNING: Use --enable-native-access=org.openjdk.jextract to avoid a warning for this module

/Users/colbychance/Library/Java/JavaVirtualMachines/openjdk-19/Contents/Home/bin/java

# Vector Lib
- CPUs have register instructions that hold multiple data
- Java can enhance loops to directly use these
- API - cross platform data parallel lags with vector instructions
- Daniel Lemier - nice blog
- Vector - sequence of elements. Each element is a lane
- Vector mask - sequence or Boolean’s, control of operation is applied to
- Vector Species - manages vectors, masks, and shuffles
- VectorShape - selects the implementation of vector
- Lanewise operations
- Cross lane
- Gives you more control over cpu - can break it down to low level and optimize in such a way that the normal compiler could not
- Can be applied to quicksort