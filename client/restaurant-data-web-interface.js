function getRestaurants(postcode = null) {
    fetch(`http://localhost:8088/byPostcode/getFirst10Restaurants${postcode ? "?postcode=" + postcode : ""}`)
        // If postcode is not falsey, then it is added as a request parameter. 
        // Else if argument is falsey, no query parameter is sent in the request, in this case in the backend a default postcode will be searched for.
        // This accounts for if the form submitted was empty, i.e. method was called with no parameter or an empty string.
        .then(res => {
            if (!res.ok) {
                const postcode_data_unavailable_content = `<p> Sorry we can't find any restaurants with postcode '${postcode}'. Please enter another postcode. </p>`;
                document.getElementById("card-group-to-populate").innerHTML = postcode_data_unavailable_content;
                throw new Error('server response or client request was not ok.');
            }
            console.log("Success")
            return res.json();      // a json array is received from the enpdoint in 'res' and parsed into a JavaScript object using json().
        })
        .then(data => {
            let content = ``;
            data.forEach(restaurant => {
                // Nested value in received resturant object is a JSON string, parse these into javascript objects.
                let ratingJsObject = JSON.parse(restaurant.ratings.rating);
                let addressJsObject = JSON.parse(restaurant.address.address);

                // Build the content by populating each card with restaurant data.
                content += `
                    <div class="card m-4  col-lg-3 col-md-6 col-12" style="width: 18rem;">
                        <div class="card-body">
                            <h5 class="card-title">${restaurant.name}</h5>
                            <h6 class="card-subtitle mb-2 text-body-secondary">Star Rating: ${ratingJsObject.starRating}</h6>
                            <p class="card-text">
                                <address> 
                                ${addressJsObject.firstLine} <br>
                                ${addressJsObject.city}<br>
                                ${addressJsObject.postalCode}<br>
                                </address>
                            </p>
                            <details>
                            <summary class="cursor-pointer">Cuisines</summary>
                            <ul class="list-group list-group-flush">`;
                restaurant.cuisines.forEach(cuisine => {
                    content += `<li class="list-group-item">${cuisine}</li>`;
                })
                content += `
                            </ul>
                            </details>
                        </div>
                    </div>`;
            });

            // Set the html element 'card-group-to-populate' which is a fluid container to the list of cards created in the forEach block above.
            document.getElementById("card-group-to-populate").innerHTML = content;
        })
        .catch(e => {
            console.error('There was a problem with the fetch operation:', e);
        });
}

// Call getRestaurants initially to fetch data when the page loads.
getRestaurants();

// Event handler for form submission event.
document.getElementById('postcode-form').addEventListener('submit', function (event) {
    // First parameter of callack function is automatically populated with an event object which contains associated information about the event.
    event.preventDefault();     // Prevent the default form submission behavior associated with event e.g. using action, method attributes to send the form.
    const postcode = document.getElementById('postcode').value;     // Retrieves value entered into an input field.
    getRestaurants(postcode);   // Fetch data with the postcode the user entered.
});