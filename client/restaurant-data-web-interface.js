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
            data.forEach(restaurant => {
                console.log(restaurant.name);
                console.log(restaurant.cuisines);
                // Nested value in received resturant object is a JSON string, parse these into a javascript objects
                let ratingJsObject = JSON.parse(restaurant.ratings.rating);
                let addressJsObject = JSON.parse(restaurant.address.address);
                console.log(ratingJsObject);
                console.log(addressJsObject);
                console.log("xxxxxxxxxxxxxxxxxxxxx")
            });
        })
        .catch(e => {
            console.error('There was a problem with the fetch operation:', e);
        });
}

getRestaurants();