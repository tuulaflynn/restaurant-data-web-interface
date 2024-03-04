package com.restaurantdata.proxyserverforwebinterface.controller;

import com.restaurantdata.proxyserverforwebinterface.model.RestaurantDto;
import com.restaurantdata.proxyserverforwebinterface.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("byPostcode")       // All request according to postcodes will be in this controller.
// future edit: allows requests from only ports running on either the domain '127.0.0.1' or 'localhost',
// @CrossOrigin(origins = {"http://localhost:[*]", "http://127.0.0.1:[*]"}) doesn't work
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/cors/CorsConfiguration.html#setAllowedOriginPatterns(java.util.List)
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired      // Constructor injection over field injection as it is more robust.
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/getFirst10Restaurants")
    public ResponseEntity<List<RestaurantDto>> getFirst10Restaurants(@RequestParam(required = false) String postcode) {
        if (postcode == null || postcode.equals("") ) {
            postcode = "EC4M7RF";   // Default postcode to search for if parameter is not provided in request.
        }
        return new ResponseEntity<>(restaurantService.getFirst10Restaurants(postcode), HttpStatus.OK);
    }
}
