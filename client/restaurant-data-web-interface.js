function getRestaurants() {
    fetch(`http://localhost:8088/byPostcode/getFirst10Restaurants`)
        .then(res => {
            if (!res.ok) {
                throw new Error('Response was not ok');
            }
            console.log("Success")
            return res
        })
        .catch(e => {
            console.error('There was a problem with the fetch operation:', e);
        });
}

getRestaurants();