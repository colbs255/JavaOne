package panama;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.MemorySession;
import java.lang.invoke.MethodHandle;
import static java.lang.foreign.ValueLayout.JAVA_INT;
import static java.lang.foreign.ValueLayout.ADDRESS;

// java --enable-preview --source 19 --enable-native-access=ALL-UNNAMED Simple.java
public class Panama {

    public static void main(String[] args) throws Throwable {
        simpleDowncall();
    }

    // Downcall: call lower level code from higher level code (java calling c code)
    public static void simpleDowncall() throws Throwable {
        String payload = "21";
        var functionName = "atoi";
        // Enables linking between c/c++
        Linker linker = Linker.nativeLinker();
        SymbolLookup linkerLookup = linker.defaultLookup();
        // Lookup the function (you don't have the type info though)
        MemorySegment memSegment = linkerLookup.lookup(functionName).get();
        FunctionDescriptor funcDescriptor = FunctionDescriptor.of(JAVA_INT, ADDRESS);
        MethodHandle funcHandle = linker.downcallHandle(memSegment.address(), funcDescriptor);

        try (var memorySession = MemorySession.openConfined()) {
            var segment = memorySession.allocateUtf8String(payload);
            int result = (int) funcHandle.invoke(segment);
            System.out.println("The answer is " + result*2);
        }
    }

    // Upcall: call upper level code from lower level code (c calling java code)
    public static void simpleUpcall() {

    }
}