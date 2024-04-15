const currentLatitude = 33.450701;
const currentLongitude = 126.570667;

const currentLocationMarker = {
    "category": "내위치",
    "latitude": currentLatitude,
    "longitude": currentLongitude
};

var container = document.getElementById('map');
var options = {
    center: new kakao.maps.LatLng(currentLocationMarker.latitude, currentLocationMarker.longitude),
};
var map = new kakao.maps.Map(container, options);
const bounds = new kakao.maps.LatLngBounds();
createMarker(currentLocationMarker);

// 마커 생성
markerInfo.forEach(function(marker) {
    createMarker(marker);
});
// 맵 범위 조정
map.setBounds(bounds);

// 목록 보기
var markerList = document.getElementById('markerList');
var markerNameContainer = document.getElementById('markerName');
var markerPriceContainer = document.getElementById('markerPrice');

// 마커 정보를 이용하여 목록 생성
markerInfo.forEach(function(marker, index) {
    var listItem = document.createElement('li');
    listItem.textContent = marker.name;
    listItem.dataset.price = marker.price; // 마커의 가격 정보를 data 속성으로 추가
    markerList.appendChild(listItem);

    // 목록 제목을 클릭했을 때 이름과 가격 정보를 표시하는 이벤트 리스너 추가
    listItem.addEventListener('click', function() {
        var price = marker.price;
        var name = marker.name;
        if (price) {
            markerNameContainer.textContent = name;
            markerPriceContainer.textContent = "가격: " + price + "원";
        } else {
            markerNameContainer.textContent = name;
            markerPriceContainer.textContent = "무료 & 가격정보 없음?";
        }
    });
});

// 함수 =============================================================================================
// 마커생성, 맵 범위 조정, 표시
function createMarker(markerInfo, marker_img_src = undefined) {
    const markerPosition = new kakao.maps.LatLng(markerInfo.latitude, markerInfo.longitude);

    if (marker_img_src == undefined) {
        const content = markerInfo.category == '내위치' ? `<div class="marker_home">🤔</div>`
                                                        : `<div class="marker_category">${markerInfo.category}</div>`;

        const categoryOverlay = new kakao.maps.CustomOverlay({
            position: markerPosition,
            content: content
        });
        categoryOverlay.setMap(map);
    } else {
        var markerImage = new kakao.maps.MarkerImage(marker_img_src, marker_img_size, marker_img_option);
        var marker = new kakao.maps.Marker({
            position: markerPosition,
            image: markerImage,
        });
        marker.setMap(map);
    }
    bounds.extend(markerPosition);
}
