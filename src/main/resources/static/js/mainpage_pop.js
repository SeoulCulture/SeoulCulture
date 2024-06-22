// 데이터를 배열로 관리
const items = [
    { text: "OO행사!\n(06.22~06.30)", url: "https://example.com/event" },
    { text: "✉️문자알림 신청", url: "/subscribe", isModal: true, modal: "subscribe" }
];


document.addEventListener("DOMContentLoaded", () => {
    const modal = document.querySelector(".modal");

    const popupBody = document.querySelector(".popup-body");
    items.forEach(item => {
        addItem(popupBody, item);
    });

    // 모달 관련 요소들
    const span = document.getElementsByClassName("close")[0];

    // 모달 닫기 이벤트
    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
});

function addItem(parent, item) {
    const newItem = document.createElement("div");
    newItem.className = "popup-item";
    newItem.textContent = item.text;

    if (item.isModal) {
        newItem.addEventListener("click", () => {
            openModal(item.modal);
        });
    } else {
        newItem.addEventListener("click", () => {
            sendRequest(item.url);
        });
    }

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

function openModal(modalName) {
    if (modalName === "subscribe") {
        const modal = document.getElementById("subs-modal");
        modal.style.display = "block";
    }
}