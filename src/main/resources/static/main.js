const searchBtn = document.getElementById('search');
const resetBtn = document.getElementById('reset');
const table = document.getElementById('table');
const loadingRow = document.getElementById('loading_row');
const loadingMessage = document.getElementById('loading_message');

let isLoading = false;

const getData = () => {
    const plz = document.getElementById('plz').value;
    const pricefrom = document.getElementById('pfrom').value;
    const priceto = document.getElementById('pto').value;
    const areafrom = document.getElementById('afrom').value;
    const areato = document.getElementById('ato').value;
    const roomCount = document.getElementById('rcount').value;

    console.log('Searchbutton clicked');
    searchBtn.disabled = true;

    if (plz === '' || pricefrom === '' || priceto === '' || areafrom === '' || areato === '' || roomCount === '') {
        alert("Bitte füllen Sie alle benötigten Felder aus ");
        searchBtn.disabled = false;
    } else {
        const url = 'http://localhost:8080/willbot/api/request?postCode=' + plz + '&priceTo=' + priceto + '&priceFrom=' + pricefrom;

        const xhr = new XMLHttpRequest();
        xhr.open('GET', url);
        xhr.responseType = 'json';

        xhr.onload = () => {
            let data = xhr.response;
            if (!data || data === '') {
                console.log('Invalid Input');
            } else {
                let output = [];

                for (let i = 0; i < data.length; i++) {
                    output.push(`<a href="${data[i].href}">${data[i].href}</a>`);
                    output.push(data[i].title);
                    output.push(data[i].postCode);
                    output.push(data[i].buy);
                    output.push(data[i].price);
                    output.push(data[i].livingArea);
                    output.push(data[i].roomCount);
                    output.push(data[i].provider);

                }

                let result = [];

                let i = 0;
                let r = 0;

                while (i < output.length) {
                    result[r] = new Array(8);
                    for (j = 0; j < 8; j++) {
                        result[r][j] = output[i];
                        i++;
                    }
                    r++;
                }

                let c = 0;

                if (result.length === 0) {
                    alert('Keine passende Ergebnisse gefunden');
                } else {
                    let columnCount = result[0].length;
                    for (let i = 1; i < result.length; i++) {
                        if (result[i][columnCount - 2] < roomCount) {
                            console.log('roomcount filtered');
                        } else {
                            if(result[i][columnCount - 3] < areafrom){
                                console.log('areafrom filtered');
                            }else {
                                if(result[i][columnCount - 3] > areato){
                                    console.log('areato filtered');
                                }else {
                                    let row = table.insertRow(-1);
                                    c++;
                                    for (let j = 0; j < columnCount; j++) {
                                        let cell = row.insertCell(-1);
                                        cell.innerHTML = result[i][j];
                                    }
                                }
                            }
                        }
                    }
                } if(c === 0) {
                    alert('Keine passenden Ergebnisse gefunden');
                }
                isLoading = false;
                loadingMessage.textContent = '';
                loadingRow.hidden = true;
                searchBtn.disabled = false;
            }
        };

        xhr.send();
        isLoading = true;
        whileLoading();
        animateSearchButton();
        console.log('searching......please be patient with me');
    }
};
searchBtn.addEventListener('click', getData);


const resetData = () => {
    console.log('Resetbutton clicked');
    window.location.reload();
};
resetBtn.addEventListener('click', resetData);

const loading_messages = [
    '⏳ Bitte hab Geduld mit mir. Seiten durchsuchen ist mühsam.',
    '🔎 Ich klappere die Seiten ab...',
    '☕ Du kannst dir daweil einen Kaffe holen...',
    '🍵 Oder einen Tee...',
    '⏳ Ein Bisschen noch...',
    '💡 Gleich hab ich\'s!',
    '😕 Oder auch nicht....',
    '🐢 Nein, Das bin nicht ich! Das Internet ist so langsam.',
    '🤔 So große Seiten, und können sich kein schnelleres Netz leisten...',
];

let messageIndex = 0;
function whileLoading() {
    if (isLoading) {
        loadingMessage.textContent = loading_messages[messageIndex];
        messageIndex = (messageIndex + 1) % loading_messages.length;
        loadingRow.hidden = false;
        setTimeout(whileLoading, 10000);
    } else {
        loadingMessage.textContent = '';
        loadingRow.hidden = true;
    }
}

let maxDots = 5;
let currentDots = 0;
function animateSearchButton() {
    if (isLoading) {
        searchBtn.textContent = getDots();
        setTimeout(animateSearchButton, 1000);
    } else {
        searchBtn.textContent = 'Suchen';
    }
}

function getDots() {
    let dots = '';
    for (let i = 0; i <= currentDots; i++) {
        dots += 'o';
    }
    currentDots = (currentDots + 1) % maxDots;
    return dots;
}
