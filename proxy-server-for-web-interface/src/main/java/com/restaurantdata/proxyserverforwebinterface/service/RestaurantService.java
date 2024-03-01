package com.restaurantdata.proxyserverforwebinterface.service;

import com.restaurantdata.proxyserverforwebinterface.model.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> getFirst10Restaurants();

}
