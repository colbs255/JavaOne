package org.j19lab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
//        { // Fails - overflow
//            var input = new Random(209).ints(32L)
//                    .boxed()
//                    .collect(Collectors.toCollection(ArrayList::new));
//            Collections.sort(input, (a, b) -> a - b);
//        }

        { // Fails due to autounboxing Using Integers (a == b)
            var input = new Random(2664).ints(32L, 1000, 1100)
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.sort(input, (a, b) -> a < b ? -1 : a == b ? 0 : 1);
        }

        { // Works - no auto unboxing
            var input = new Random(2664).ints(32L, 1000, 1100)
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.sort(input, (a, b) -> a < b ? -1 : b < a ? 1 : 0);
        }
    }
}