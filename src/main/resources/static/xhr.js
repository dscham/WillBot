const searchBtn = document.getElementById('search');
const resetBtn = document.getElementById('reset');
const input = document.getElementById('input');



const getData = () => {

    var plz = document.getElementById('plz').value;
    var pricefrom = document.getElementById('pfrom').value;
    var priceto = document.getElementById('pto').value;

    var areafrom = document.getElementById('afrom').value;
    var areato = document.getElementById('ato').value;
    var roomCount = document.getElementById('rcount').value;

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

                var output = [];

                for (var i = 0; i < data.length; i++) {
                    output.push(data[i].href);
                    output.push(data[i].title);
                    output.push(data[i].postCode);
                    output.push(data[i].buy);
                    output.push(data[i].price);
                    output.push(data[i].livingArea);
                    output.push(data[i].roomCount);
                    output.push(data[i].provider);
                }

                var result = [];


                var i = 0;
                var r = 0;


                while(i < output.length) {
                    result[r] = new Array(8);
                    for(j = 0; j < 8; j++){
                        result[r][j] = output[i];
                        i++;
                    }
                    r++;
                }

                var columnCount = result[0].length;

                for (var i = 1; i < result.length; i++) {
                    var row = table.insertRow(-1);
                    for (var j = 0; j < columnCount; j++) {
                        var cell = row.insertCell(-1);
                        cell.innerHTML = result[i][j];
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
