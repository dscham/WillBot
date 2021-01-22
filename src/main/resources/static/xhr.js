//creating variables for the buttons and the loader

const searchBtn = document.getElementById('search');
const resetBtn = document.getElementById('reset');

const loader = document.getElementById('loader');



//Method for Searchbutton
const getData = () => {
    //making the loader visible and disabeling the searchubutton
    loader.style.display = "block";
    searchBtn.disabled = true;


    //making variables from the input fields
    var plz = document.getElementById('plz').value;
    var pricefrom = document.getElementById('pfrom').value;
    var priceto = document.getElementById('pto').value;

    var areafrom = document.getElementById('afrom').value;
    var areato = document.getElementById('ato').value;
    var roomCount = document.getElementById('rcount').value;



    console.log('Searchbutton clicked');
    const xhr = new XMLHttpRequest();

    var table = document.getElementById('table')


    //checking if all fields have an input
    if (plz === '' || pricefrom === '' || priceto === '' || areafrom === '' || areato === '' || roomCount === '') {
        alert("Bitte füllen Sie alle benötigten Felder aus ");
    } else {

        //request to server and getting json data back
        const url = 'http://localhost:8080/willbot/api/request?postCode=' + plz + '&priceTo=' + priceto + '&priceFrom=' + pricefrom;
        console.log('searching......please be patient with me');
        xhr.open('GET', url);
        xhr.responseType = 'json';

        xhr.onload = () => {
            //checking if there is data
            var data = xhr.response;
            if (data === '') {
                console.log('Invalid Input');
            } else {

                var output = [];


                //making an array out of the json data
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

                //making a 2dimensional array out of our first array
                while (i < output.length) {
                    result[r] = new Array(8);
                    for (j = 0; j < 8; j++) {
                        result[r][j] = output[i];
                        i++;
                    }
                    r++;
                }

                var c = 0;

                //Cheking the output
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
                                        //creating the rows that get filled into the table
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
                    //enanbeling the search button and hiding the loader
                    searchBtn.disabled = false;
                    loader.style.display = "none";
                }
            }
        };

        xhr.send();
    }
};


//Method for reset button
//Resets the whole Page
const resetData = () => {
    console.log('Resetbutton clicked');
    window.location.reload();
};

// creating event listeners for the buttons
searchBtn.addEventListener('click', getData);
resetBtn.addEventListener('click', resetData);








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
