package com.CommerceSpring.dto.request;

public record PageRequestDto(
    String searchText,
    int page,
    int size)
{
}
