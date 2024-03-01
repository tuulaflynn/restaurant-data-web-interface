package com.restaurantdata.proxyserverforwebinterface.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class RestaurantDto {
    private String name;
    private List<String> cuisines;
    private Integer ratings;
    private String address;
}
