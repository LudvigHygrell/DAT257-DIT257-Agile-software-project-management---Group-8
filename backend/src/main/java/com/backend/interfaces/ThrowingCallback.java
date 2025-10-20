package com.backend.interfaces;

public interface ThrowingCallback<T> {
    
    public void perform(T val) throws Exception;
}
