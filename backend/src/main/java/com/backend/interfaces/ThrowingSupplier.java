package com.backend.interfaces;

@FunctionalInterface
public interface ThrowingSupplier<R> {

    public R get() throws Exception;
}
