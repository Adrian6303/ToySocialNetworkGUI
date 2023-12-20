package com.example.toysocialnetworkgui.repository.paging;

import com.example.toysocialnetworkgui.domain.Entity;
import com.example.toysocialnetworkgui.repository.Repository;

public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
}
