package com.restaurantdata.proxyserverforwebinterface.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.restaurantdata.proxyserverforwebinterface.model.RestaurantDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Override
    public List<RestaurantDto> getFirst10Restaurants() {
        List<RestaurantDto> restaurantsList = new ArrayList<RestaurantDto>();

        String postcode = "EC4M7RF";		// In future the interface could allow user to choose passcode. This variable can be sent in request.
        String dataJsonString = getDataByPostcode(postcode);
        JsonArray arrayOfJsonObjects = extractValueFromJsonString(dataJsonString, "restaurants");

        if (arrayOfJsonObjects == null) {
            // Error handling could be added here to throw custom error 'no data found for restaurants'.
            return null;
        }

        for (int i = 0; i < 10; i++) {
            JsonObject jsonObject = arrayOfJsonObjects.get(i).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();

            List<String> cuisinesList = new ArrayList<>();
            JsonArray cuisinesArray = jsonObject.getAsJsonArray("cuisines");
            for (JsonElement element : cuisinesArray) {
                String cuisineName = element.getAsJsonObject().get("name").getAsString();
                cuisinesList.add(cuisineName);
            }

            Map<String, Object> ratingMap = new HashMap<>();
            String ratingJsonString = jsonObject.getAsJsonObject("rating").toString();
            ratingMap.put("rating", ratingJsonString);

            Map<String, Object> addressMap = new HashMap<>();
            String addressJsonString = jsonObject.getAsJsonObject("address").toString();
            addressMap.put("address", addressJsonString);

            RestaurantDto restaurantDto = new RestaurantDto(name, cuisinesList, ratingMap, addressMap);
            restaurantsList.add(restaurantDto);
        }
        return restaurantsList;
    }

    private String getDataByPostcode(String postcode) {
        // Method passes the response body received from the endpoint
        String apiEndpoint = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode/" + postcode;

        // RestTemplate class for client-side HTTP access.
        RestTemplate restTemplate = new RestTemplate();
        // Sends a get request (which follows RESTful principles) to specified endpoint and returns the response.
        // Second param is the type of response body expected.
        ResponseEntity<String> res = restTemplate.getForEntity(apiEndpoint, String.class);
        // The second param I could have a used an object to represent the JSON object which was composed of a Restaurant object.
        // If the fields in the java class match fields in the JSON response the response would be converted into an
        // instance populated will all these fields (deserialization).

        return res.getBody();
    }

    private JsonArray extractValueFromJsonString(String jsonString, String key) {
        // This method extracts a value which is of the type json array, an array of json objects, from a json string.
        // Code from the internet helped me make this method.
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Get the value from it's key-value pair.=
        JsonElement value = jsonObject.get(key);

        // Check if the value is not null, if so then check if it doesn't represent a JSON null value. Avoids possible NullPointerException.
        if (value != null && !value.isJsonNull()) {
            return value.getAsJsonArray(); // The value being extracted is an array of JSON objects.
        } else {
            // Value does not exist in the JSON. If I throw custom exception here I don't have to re handle it in the calling method.
            // COME BACK TO THIS HANDLING
            return null;
        }
    }


}
