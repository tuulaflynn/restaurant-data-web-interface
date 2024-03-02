package com.restaurantdata.proxyserverforwebinterface.model;

import com.google.gson.JsonElement;
import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RestaurantDto {
    private String name;
    private List<String> cuisines;
    private Map<String, Object> ratings;
    private Map<String, Object>  address;
}
