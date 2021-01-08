const searchBtn = document.getElementById('search');
const resetBtn = document.getElementById('reset');
//const input = document.getElementById('input');


const getData = () => {

    var plz = document.getElementById('plz').value;
    var pricefrom = document.getElementById('pfrom').value;
    var priceto = document.getElementById('pto').value;

    var areafrom = document.getElementById('afrom').value;
    var areato = document.getElementById('ato').value;
    var roomCount = document.getElementById('rcount').value;


    /*
    var provider = document.querySelectorAll('input[name="provider"]');
    var willhaben = true;
    var immowelt = false;
    if (provider[0].checked === true) {
            willhaben = true;
    }else{
        willhaben = false;
    }
    if (provider[1].checked === true) {
        immowelt = true;
    }else{
        immowelt = false;
    }
    */


    console.log('Searchbutton clicked');
    const xhr = new XMLHttpRequest();

    var table = document.getElementById('table')


    if (plz === '' || pricefrom === '' || priceto === '' || areafrom === '' || areato === '' || roomCount === '') {
        alert("Bitte füllen Sie alle benötigten Felder aus ");
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


                while (i < output.length) {
                    result[r] = new Array(8);
                    for (j = 0; j < 8; j++) {
                        result[r][j] = output[i];
                        i++;
                    }
                    r++;
                }

                var c = 0;

                if (result.length === 0) {
                    alert('Keine passende Ergebnisse gefunden');
                } else {
                    var columnCount = result[0].length;
                    for (var i = 1; i < result.length; i++) {
                        if (result[i][columnCount - 2] < roomCount) {
                            console.log('roomcount filtered');
                        } else {
                            /*if(immowelt === false && result[i][columnCount - 1] ==='Immowelt'){
                                console.log('immowelt filtered');
                            }else {*/
                                if(result[i][columnCount - 3] < areafrom){
                                    console.log('areafrom filtered');
                                }else {
                                    if(result[i][columnCount - 3] > areato){
                                        console.log('areato filtered');
                                    }else {
                                        var row = table.insertRow(-1);
                                        c++;
                                        for (var j = 0; j < columnCount; j++) {
                                            var cell = row.insertCell(-1);
                                            cell.innerHTML = result[i][j];
                                        }
                                    }
                                //}
                            }
                        }
                    }
                }if(c === 0){
                    alert('Keine passenden Ergebnisse gefunden');
                }else{
                    searchBtn.style.visibility = 'hidden';
                }
            }
        };
        xhr.send();
    }
};


const resetData = () => {
    console.log('Resetbutton clicked');
    window.location.reload();
};

searchBtn.addEventListener('click', getData);
resetBtn.addEventListener('click', resetData);
