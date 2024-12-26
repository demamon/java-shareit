package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long idOwner;
}
