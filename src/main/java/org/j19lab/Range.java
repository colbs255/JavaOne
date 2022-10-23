package org.j19lab;

public record Range(int start, int end) {

    // Creates default canonical constructor

    // Can make normal constructor or use compact constructor
    // Note that a compact constructor not only doesn't require you to assign fields, it prevents you from doing so - that's something that falls to the generated code. So if you need to assign a different value to the field than the argument, you can't do that directly. Instead, you need to reassign the desired value to the parameter.
    public Range {
        if (end < start) {
            throw new IllegalArgumentException("End must be greater than start");
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || o instanceof Range other
                && start == other.start
                && end == other.end;
    }
}
