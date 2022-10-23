package panama;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.foreign.ValueLayout.ADDRESS;

/**
 * Note: static is important here
 * Run with:
 * Compile c: gcc -fPIC -shared -o ~/mylib.so upcall.c
 * Then run the java:
 * java -Dlib.path=$PWD/lib.platform --enable-native-access=ALL-UNNAMED --enable-preview --source 19 Upcall.java
 */
public class Upcall {
    static final Linker linker = Linker.nativeLinker();
    private static final SymbolLookup linkerLookup = linker.defaultLookup();
    private static final SymbolLookup systemLookup = SymbolLookup.loaderLookup();
    /** Try system then fallback to c lookup **/
    private static final SymbolLookup symbolLookup = name ->
            systemLookup.lookup(name).or(() -> linkerLookup.lookup(name));
    static void callback() {
        System.out.println("Java\t callback() method");
    }

    private static final MethodHandle callbackHandle;
    static {
        System.load(System.getProperty("lib.path"));
        try {
            // Method Handle for the Upcall#callback method.
            callbackHandle = MethodHandles.lookup()
                    .findStatic(Upcall.class, "callback", MethodType.methodType(void.class));
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static final MethodHandle nativeFunctionWithCallback = symbolLookup.lookup("callback_function")
            .map(add -> linker.downcallHandle(add, FunctionDescriptor.ofVoid(ADDRESS))).orElseThrow();
    static final FunctionDescriptor callbackDescriptor = FunctionDescriptor.ofVoid();

    public static void main(String[] args) throws Throwable {
        System.out.println("Java\t main() method");

        var ms = MemorySession.openConfined();
        var callbackNativeSymbolSegment = linker.upcallStub(callbackHandle, callbackDescriptor, ms);

        nativeFunctionWithCallback.invokeExact((Addressable) callbackNativeSymbolSegment);

        System.out.println("Java\t main() method exit");
    }

}