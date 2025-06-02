package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
public class ItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String description;
    LocalDateTime created;
}
