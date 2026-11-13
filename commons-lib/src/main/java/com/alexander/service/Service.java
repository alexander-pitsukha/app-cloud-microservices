package com.alexander.service;

import com.alexander.dto.AbstractLongIdentifiableDto;

import java.io.Serializable;
import java.util.Collection;

public interface Service<D extends AbstractLongIdentifiableDto, P, L extends Collection<?>, I extends Serializable> {

    P findAll(int page, int size);

    L findAutocomplete(String query, Integer limit);

    D findById(I id);

    D save(D d);

    D update(I id, D d);

    void deleteById(I id);

}
