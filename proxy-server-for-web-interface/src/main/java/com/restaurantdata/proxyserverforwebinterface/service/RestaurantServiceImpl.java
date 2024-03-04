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
    public List<RestaurantDto> getFirst10Restaurants(String postcode) {
        // Method returns a list of 10 RestaurantDto objects for a given postcode.

        List<RestaurantDto> restaurantsList = new ArrayList<RestaurantDto>();

        String dataJsonString = getDataByPostcode(postcode);
        JsonArray restaurantsJsonArray = extractValueFromJsonString(dataJsonString, "restaurants");

        if (restaurantsJsonArray == null) {
            // Error handling could be added here to throw custom error 'no data found for restaurants'.
            return null;
        }

        for (int i = 0; i < 10; i++) {
            // Loops through the first 10 elements from the restaurant JSON array and returns them as a restaurantDto list.
            JsonObject restaurantJsonObject = restaurantsJsonArray.get(i).getAsJsonObject();

            String name = restaurantJsonObject.get("name").getAsString();

            // Each restaurant JSON object in the restaurants JSON array is composed of a cuisines key which has a value of a JSON array.
            List<String> cuisinesList = new ArrayList<>();
            JsonArray cuisinesArray = restaurantJsonObject.getAsJsonArray("cuisines");
            // Retrieves the string value from each JSON object in the 'cuisines' JSON array. Stores these values (strings) as a list.
            for (JsonElement element : cuisinesArray) {
                // JSON element type is necessary as JSON arrays can be of all JSON types (here, all my elements are JSON objects).
                String cuisineName = element.getAsJsonObject().get("name").getAsString();
                cuisinesList.add(cuisineName);
            }

            // Each restaurant JSON object is composed of a rating key which has a value with type JSON object.
            Map<String, String> ratingMap = new HashMap<>();
            // Convert JSON object into its string representation. It returns the JSON data as a string.
            // This string representation needs to be parsed/extracted again on the client side to obtain the actual JSON data.
            String ratingJsonString = restaurantJsonObject.getAsJsonObject("rating").toString();
            ratingMap.put("rating", ratingJsonString);

            // Each restaurant JSON object is composed of an address key which has a value with type JSON object.
            Map<String, String> addressMap = new HashMap<>();
            // Convert the value to the string representation of the JSON object.
            String addressJsonString = restaurantJsonObject.getAsJsonObject("address").toString();
            addressMap.put("address", addressJsonString);

            RestaurantDto restaurantDto = new RestaurantDto(name, cuisinesList, ratingMap, addressMap);
            restaurantsList.add(restaurantDto);
        }
        return restaurantsList;
    }

    private String getDataByPostcode(String postcode) {
        // Method returns the response body received from the api via the endpoint given below.

        String apiEndpoint = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode/" + postcode;

        RestTemplate restTemplate = new RestTemplate();     // RestTemplate class for client-side HTTP access.
        ResponseEntity<String> res = restTemplate.getForEntity(apiEndpoint, String.class);      // Sends a get request
        // to specified endpoint and returns the response. The second param is the type of response body expected.

        // In future if more of the data from the response json object is needed could use deserialization.
        // - use an object to represent the JSON object (hence it would be composed of a many other object e.g. Restaurant object),
        // if the fields in the java class match fields in the JSON object, the response would be converted into an
        // instance will all fields populated.

        return res.getBody();
    }

    private JsonArray extractValueFromJsonString(String jsonString, String key) {
        // This method extracts a value which is of the type json array (an array of json objects) from a json string.
        // Code from the internet helped me make this method.

        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement value = jsonObject.get(key);    // Get the value from it's key-value pair.

        if (value != null && !value.isJsonNull()) {
            // Check if the value is not null, if so, then check if it doesn't represent a JSON null value. Avoids possible NullPointerException.
            return value.getAsJsonArray();          // The value being extracted is an array of JSON objects.
        } else {           // Value does not exist in the JSON.
            return null;   // Error handling could be added here (or in the calling method) to throw custom error 'no data found for restaurants'.
        }
    }


}
