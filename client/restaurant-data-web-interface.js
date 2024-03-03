function getRestaurants() {
    fetch(`http://localhost:8088/byPostcode/getFirst10Restaurants`)
        .then(res => {
            if (!res.ok) {
                throw new Error('Response was not ok');
            }
            console.log("Success")
            return res.json(); // a json array is received from the enpdoint in 'res' and parsed into a JavaScript object using json().
        })
        .then(data => {
            let content = ``;
            data.forEach(restaurant => {
                // Nested value in received resturant object is a JSON string, parse these into a javascript objects
                let ratingJsObject = JSON.parse(restaurant.ratings.rating);
                let addressJsObject = JSON.parse(restaurant.address.address);

                // Build the content to put into populate each card with restaurant data
                content += `
                    <div class="card m-4 border" style="width: 18rem;">
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

            // Set the html element 'card-group-to-populate' which is a fluid container to the list of cards created in the forEach method above
            document.getElementById("card-group-to-populate").innerHTML = content;
        })
        .catch(e => {
            console.error('There was a problem with the fetch operation:', e);
        });
}

getRestaurants();