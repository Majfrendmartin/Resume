package com.wec.resume.model;


public class ModalPair<T, V> {
    public T first;
    public V second;

    public ModalPair(T first, V second) {
        this.first = first;
        this.second = second;
    }

    public static final <T, V> ModalPair<T, V> create(T first, V second) {
        return new ModalPair<>(first, second);
    }
}
