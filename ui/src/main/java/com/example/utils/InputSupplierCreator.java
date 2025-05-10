package com.example.utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class that creates suppliers for input processing.
 * It combines a {@link Consumer} for displaying messages, a {@link Supplier} for obtaining input,
 * and a {@link Function} for converting the input into a desired type.
 *
 * @param <T> the target type of the input after conversion
 * @param <K> the intermediate type of the input
 */
public class InputSupplierCreator<T, K> {

    protected Consumer<String> consumer;

    protected Supplier<K> supplier;

    protected Function<K, T> converter;

    /**
     * Constructor for creating an InputSupplierCreator with a consumer, supplier, and a converter.
     *
     * @param consumer  the {@link Consumer} that displays a message
     * @param supplier  the {@link Supplier} that obtains input data
     * @param converter the {@link Function} to convert the input data into the target type
     */
    public InputSupplierCreator(Consumer<String> consumer, Supplier<K> supplier, Function<K, T> converter) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.converter = converter;
    }

    /**
     * Constructor for creating an InputSupplierCreator with a consumer and a supplier.
     * A default converter is used, which casts the input into the desired type.
     *
     * @param consumer the {@link Consumer} that displays a message
     * @param supplier the {@link Supplier} that obtains input data
     */
    public InputSupplierCreator(Consumer<String> consumer, Supplier<K> supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.converter = s -> (T) s;
    }

    /**
     * Creates a {@link Supplier} that takes a message to display to the user,
     * obtains input using the supplier, and converts the input into the target type.
     *
     * @param message the message to display to the user before accepting input
     * @return a {@link Supplier} that provides the converted input
     */

    public Supplier<T> supplier(String message) {
        consumer.accept(message);
        return () -> converter.apply(supplier.get());
    }
}
