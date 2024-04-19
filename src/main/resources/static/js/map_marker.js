
var map;
var bounds;
const currentLatitude = localStorage.getItem("currentLatitude");
const currentLongitude = localStorage.getItem("currentLongitude");
console.log(currentLatitude, currentLongitude);
// 지도 초기화
function initMap() {
    var container = document.getElementById('map');
    var options = {
        center: new kakao.maps.LatLng(currentLatitude,currentLongitude),
    };
    map = new kakao.maps.Map(container, options);
    bounds = new kakao.maps.LatLngBounds();
    const currentLocationMarker = {
        "latitude": currentLatitude,
        "longitude": currentLongitude
    };
    createMarker(currentLocationMarker, '<div class="marker_home">🤔</div>');
}

// 맵 초기화
initMap();

// 마커 생성
markerInfo.forEach(function (marker) {
    createMarker(marker);
});

// 맵 범위 조정
map.setBounds(bounds);

// 목록 보기
var markerList = document.getElementById('markerList');
var markerNameContainer = document.getElementById('markerName');
var markerPriceContainer = document.getElementById('markerPrice');

// 마커 정보를 이용하여 목록 생성
markerInfo.forEach(function (marker, index) {
    var listItem = document.createElement('li');
    listItem.textContent = marker.title;
    listItem.dataset.price = marker.price; // 마커의 가격 정보를 data 속성으로 추가
    markerList.appendChild(listItem);

    // 목록 제목을 클릭했을 때 이름과 가격 정보를 표시하는 이벤트 리스너 추가
    listItem.addEventListener('click', function () {
        var price = marker.price;
        var title = marker.title;
        if (price) {
            markerNameContainer.textContent = title;
            markerPriceContainer.textContent = "가격: " + price + "원";
        } else {
            markerNameContainer.textContent = title;
            markerPriceContainer.textContent = "무료 & 가격정보 없음?";
        }
    });
});


// 함수 =============================================================================================
// 마커생성, 맵 범위 조정, 표시
function createMarker(markerInfo, customHtmlContents = undefined) {
    const markerPosition = new kakao.maps.LatLng(markerInfo.latitude, markerInfo.longitude);
    let content;
    if (customHtmlContents == undefined) {
        content = `<div class="marker_category">${markerInfo.category}</div>`;
    } else {
        content = customHtmlContents;
    }

    const customOverlay = new kakao.maps.CustomOverlay({
        position: markerPosition,
        content: content
    });
    customOverlay.setMap(map);
    bounds.extend(markerPosition);
}