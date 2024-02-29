function getRestaurants() {
    fetch(`http://localhost:8088/proxy`)
        .then(res => {
            if (!res.ok) {
                throw new Error('Response was not ok');
            }
            return res.json();
        })
        .then(data => {
            let restaurants = data.restaurants
            console.log(restaurants);
        })
        .catch(e => {
            console.error('There was a problem with the fetch operation:', e);
        });
}

getRestaurants();