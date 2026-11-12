package com.alexander.service;

public interface Service<T, P, L, I> {

    P findAll(int page, int size);

    L findAutocomplete(String query, Integer limit);

    T findById(I id);

    T save(T t);

    T update(I id, T t);

    void deleteById(I id);

}
