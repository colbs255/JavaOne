package loom.structuredconcurrency.model;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public record Quotation(String agency, int amount) {
    private static Random random = new Random();

    public static Quotation readQuotation() {
        try (var scope = new StructuredTaskScope<Quotation>()) {
            var a = scope.fork(Quotation::readQuotationFromA);
            var b = scope.fork(Quotation::readQuotationFromB);
            var c = scope.fork(Quotation::readQuotationFromC);
            var d = scope.fork(Quotation::readQuotationFromD);
            var e = scope.fork(Quotation::readQuotationFromE);

            // JOin has to be called first
            scope.join();
            return Stream.of(a, b, c, d, e)
                    .map(Future::resultNow)
                    .min(Comparator.comparing(Quotation::amount))
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromA() {
        try {
            Thread.sleep(random.nextInt(20, 80));
            return new Quotation("Agency A", random.nextInt(80, 120));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromB() {
        try {
            Thread.sleep(random.nextInt(40, 100));
            return new Quotation("Agency B", random.nextInt(90, 130));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromC() {
        try {
            Thread.sleep(random.nextInt(30, 120));
            return new Quotation("Agency C", random.nextInt(70, 130));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromD() {
        try {
            Thread.sleep(random.nextInt(20, 110));
            return new Quotation("Agency D", random.nextInt(60, 120));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromE() {
        try {
            Thread.sleep(random.nextInt(50, 90));
            return new Quotation("Agency E", random.nextInt(70, 110));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}