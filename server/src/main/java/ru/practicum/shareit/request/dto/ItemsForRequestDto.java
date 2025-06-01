package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemsForRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;
    String name;
    long idOwner;
}
