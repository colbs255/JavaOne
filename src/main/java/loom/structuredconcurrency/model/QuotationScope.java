package loom.structuredconcurrency.model;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.concurrent.Future;

public class QuotationScope extends StructuredTaskScope<Quotation> {

    @Override
    protected void handleComplete(Future<Quotation> future) {
        switch (future.state()) {
            case RUNNING -> throw new AssertionError("Should never happen");
            case SUCCESS -> throw new AssertionError("Should never happen");
            case CANCELLED -> throw new AssertionError("Should never happen");
            case FAILED -> throw new AssertionError("Should never happen");
            default -> throw new UnsupportedOperationException("Never heard of " + future.state());
        }
    }
}
