const items = [
    { text: "OO행사!\n(06.22~06.30)", url: "/OOevent" },
    { text: "✉️문자알림 신청", url: "/subscribe" }
];

document.addEventListener("DOMContentLoaded", () => {
    const popupBody = document.querySelector(".popup-body");
    items.forEach(item => {
        addItem(popupBody, item);
    });
});

function addItem(parent, item) {
    const newItem = document.createElement("div");
    newItem.className = "popup-item";
    newItem.textContent = item.text;

    newItem.addEventListener("click", () => {
        sendRequest(item.url);
    });

    parent.appendChild(newItem);
}

function sendRequest(url) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok " + response.statusText);
            }
            return response.json(); // Assuming the response is in JSON format
        })
        .then(data => {
            console.log("Request succeeded with JSON response", data);
        })
        .catch(error => {
            console.error("Request failed", error);
        });
}