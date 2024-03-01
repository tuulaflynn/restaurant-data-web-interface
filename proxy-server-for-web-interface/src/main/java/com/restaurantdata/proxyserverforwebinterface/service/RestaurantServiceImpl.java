package com.restaurantdata.proxyserverforwebinterface.service;

import com.restaurantdata.proxyserverforwebinterface.model.RestaurantDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private ResponseEntity<String> getDataByPostcode() {
        String postcode = "EC4M7RF";		// In future the interface could allow user to choose passcode. This variable can be sent in request.
        String apiEndpoint = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode/" + postcode;

        // RestTemplate class for client-side HTTP access.
        RestTemplate restTemplate = new RestTemplate();
        // Sends a get request (which follows RESTful principles) to specified endpoint and returns the response.
        // Second param is the type of response body expected.
        ResponseEntity<String> res = restTemplate.getForEntity(apiEndpoint, String.class);
        // The second param I could have a used an object to represent the JSON object which was composed of a Restaurant object.
        // If the fields in the java class match fields in the JSON response the response would be converted into an
        // instance populated will all these fields (deserialization).

        return res;
    }

    @Override
    public List<RestaurantDto> getFirst10Restaurants() {
        return null;
    }

}
