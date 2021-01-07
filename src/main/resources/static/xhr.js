const searchBtn = document.getElementById('search');
const resetBtn = document.getElementById('reset');


const getData = () => {

    var plz = document.getElementById('plz').value;
    var pricefrom = document.getElementById('pfrom').value;
    var priceto = document.getElementById('pto').value;

    console.log('Searchbutton clicked');
    const xhr = new XMLHttpRequest();

    var table = document.getElementById('table')


    if (plz === '' || pricefrom === '' || priceto === '') {
        alert("Please fill all the required Fields");
    } else {

        const url = 'http://localhost:8080/willbot/api/request?postCode=' + plz + '&priceTo=' + priceto + '&priceFrom=' + pricefrom;
        console.log('searching......please be patient with me');
        xhr.open('GET', url);
        xhr.responseType = 'json';

        xhr.onload = () => {
            var data = xhr.response;
            if (data === '') {
                console.log('Invalid Input');
            } else {

                var result = [];


                console.log(data.length);

                for (var i = 0; i < data.length; i++) {
                    result.push(data[i].href);
                    result.push(data[i].title);
                    result.push(data[i].postCode);
                    result.push(data[i].buy);
                    result.push(data[i].price);
                    result.push(data[i].livingArea);
                    result.push(data[i].roomCount);
                    result.push(data[i].provider);
                }


                console.log('Daten gefunden');
                console.log(data)
                console.log(result);


                var count = 0;

                while (count <= result.length) {
                    var row = table.insertRow(-1);
                    for (var i = 0; i < 8; i++) {
                        var cell = row.insertCell(-1);
                        cell.innerHTML = result[count];
                        count++;
                    }
                }
            }
        };
        xhr.send();
        searchBtn.style.visibility = 'hidden';
    }
};


const resetData = () => {
    console.log('Resetbutton clicked');
    window.location.reload();
};

searchBtn.addEventListener('click', getData);
resetBtn.addEventListener('click', resetData);
