package com.restaurantdata.proxyserverforwebinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@CrossOrigin
// future edit: allows requests from only ports running on either the domain '127.0.0.1' or 'localhost',
// @CrossOrigin(origins = {"http://localhost:[*]", "http://127.0.0.1:[*]"}) doesn't work
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/cors/CorsConfiguration.html#setAllowedOriginPatterns(java.util.List)
public class ProxyServerForWebInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyServerForWebInterfaceApplication.class, args);
	}

	@GetMapping("/proxy")
	public ResponseEntity<String> proxyRequest() {
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
}
