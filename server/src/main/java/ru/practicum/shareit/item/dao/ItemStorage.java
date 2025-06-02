package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {
    Collection<Item> findByOwnerId(long id);

    List<Item> findByItemRequestId(long id);

    @Query("SELECT i FROM Item i WHERE i.itemRequest.id IN :requestId")
    List<Item> findByItemRequestIdIn(@Param("requestId") List<Long> requestId);
}
