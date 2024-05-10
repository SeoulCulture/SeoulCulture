let map;
let bounds;
let mapElement = document.getElementById('map');
const rightContainer = document.getElementById('rightContainer');
const rightContainerContent = document.getElementById('rightContainerContent');
const rightContainerEmpty = document.getElementById('rightContainerEmpty');
const noResultRightContainer = document.getElementById('noResultRightContainer');
let currentPoint;


let markerClusterPoint;

window.onload = function () {
    currentPoint = new kakao.maps.LatLng(currentLatitude, currentLongitude);
    hideRightContent();
    initMap();

    kakao.maps.event.addListener(map, 'dragend', function () {
        animateMarkers();
    });
    kakao.maps.event.addListener(map, 'zoom_changed', function () {
        animateMarkers();
    });

    document.addEventListener('keydown', function(event) {
        // ESC & existing right-container contents
        if (event.keyCode === 27 && rightContainerContent.style.display === 'block') {
            hideRightContent();
        }
    });


    let homeMarker = document.querySelector(".marker_home");
    homeMarker.addEventListener("click", (event) => {
        animateCSS(".marker_home", getRandomAnimation());
    });

    setInterval(function() {
        jumpingHomeMarker();
    }, 3000);
}



// 함수 =============================================================================================

function relocateToCenter() { // 현위치로 이동
    map.panTo(currentPoint);
}

function relocateToCluster() {
    if (markerInfo.length == 0) {
        map.panTo(markerClusterPoint);
    } else {
        map.panTo(markerClusterPoint);
    }
}

function getOverayCountRate() {
    if (Object.keys(markers).length == 0)
        return 0;
    let overlayCnt = 0;
    for (id in markers) {
        let element = document.getElementById(id);
        if (element != null)
            overlayCnt++;
    }
    return overlayCnt / Object.keys(markers).length;
}

function setCenterPoint() {
    var latlng = map.getCenter();
    var moveLatLon = new kakao.maps.LatLng((2 * currentLatitude + latlng.getLat()) / 3, (2 * currentLongitude + latlng.getLng()) / 3);
    map.setCenter(moveLatLon);

    // 수가 적으면 레벨조정 하지 않음
    if (Object.keys(overlays).length <= 20) {
        return;
    }
    // 커스텀 오버레이의 K% 이상이 보이도록 배치
    let rate = getOverayCountRate();
    if (rate == 0) {
        map.setLevel(4);
        return;
    }

    let startValue = 13;
    map.setLevel(startValue);
    while (rate >= 0.1) {
        map.setLevel(--startValue);
        rate = getOverayCountRate();
    }
    map.setLevel(startValue + 1);
    console.log("커스텀 오버레이가 보이는 비율: ", rate);
}

function getDistance(lat1,lng1,lat2,lng2) {
    function deg2rad(deg) {
        return deg * (Math.PI/180)
    }
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2-lat1);  // deg2rad below
    var dLon = deg2rad(lng2-lng1);
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c; // Distance in km
    return d;
}

function hasNoResult() {
    if (markerInfo.length == 0) {
        showNoResultRightContent();
        const customOverlay = new kakao.maps.CustomOverlay({
            position: currentPoint,
            content: "<div id='noResult' class='animate__animated animate__slideInDown'>주변에 없어요</div>"
        });
        customOverlay.setMap(map);
        bounds.extend(currentPoint);
        return;
    }
}

function initMap() {
    mapElement = document.getElementById('map');
    var options = {center: new kakao.maps.LatLng(currentLatitude, currentLongitude)};
    map = new kakao.maps.Map(mapElement, options);
    initBounds();
    initMarkers();
    animateMarkers();
    setBounds();
    initCenter();
    setCenterPoint();
    hasNoResult();
}

function initBounds() {
    bounds = new kakao.maps.LatLngBounds();
    bounds.extend(currentPoint);
}

function initCenter() {
    const currentLocationMarker = {
        "latitude": currentLatitude,
        "longitude": currentLongitude
    };
    createMarker(currentLocationMarker, "home", '<div>' +
        '<div class="lightPoint animate__animated animate__heartBeat animate__infinite animate__slower"></div>'+
        '<div class="marker_shadow"></div><div class="marker_home">' +
        '<div class="face">' +
        '    <div class="eyes">' +
        '        <div class="eye"></div>' +
        '        <div class="eye"></div>' +
        '    </div>' +
        '    <div class="tongue"></div>'+
        '</div>' +
        '<div class="arm arm-left"></div><div class="arm arm-right"></div>' +
        '</div>'
    );
    jumpingHomeMarker();
}

const overlays = {};
const markers = {};

function initMarkers() {
    let lats = 0;
    let lons = 0;
    placeInfo.forEach(function (marker) {
        let overlay = createMarker(marker, "place");
        registerOverlay(overlay, marker.id);
        markers[marker.id] = marker;
        lats += marker.latitude;
        lons += marker.longitude;
    });
    markerInfo.forEach(function (marker) {
        let overlay = createMarker(marker, "culture");
        registerOverlay(overlay, marker.id);
        markers[marker.id] = marker;
        lats += marker.latitude;
        lons += marker.longitude;
    });
    const dataLength = placeInfo.length + markerInfo.length;
    if (dataLength == 0) {
        markerClusterPoint = currentPoint;
    } else {
        markerClusterPoint = new kakao.maps.LatLng(lats / dataLength, lons / dataLength);
    }
}

function registerOverlay(overlay, id) {
    let existingOverlay = overlays[id];
    if (existingOverlay != undefined){
        existingOverlay.setMap(null);
        delete existingOverlay;
    }
    overlays[id] = overlay;
}

function setBounds() {
    map.setBounds(bounds);
}

function deletetag(input, allow) {
    var regExp;
    if(allow.length !=0)
        regExp = "<\\/?(?!(" + allow.join('|') + "))\\b[^>]*>";
    else
        regExp = "<\/?[^>]*>";

    return input.replace(new RegExp(regExp, "gi"), "");
}

function makeContents(marker) {
    function removeOuterBrTags(inputString) {
        let startIndex = 0;
        let endIndex = inputString.length;
        while (startIndex < endIndex && inputString.substr(startIndex, 4) === '<br>') {
            startIndex += 4;
        }
        while (endIndex > startIndex && inputString.substr(endIndex - 4, 4) === '<br>') {
            endIndex -= 4;
        }
        return inputString.substring(startIndex, endIndex);
    }
    if (marker.contents == undefined)
        marker.contents = "설명이 없습니다";
    else
        marker.contents = removeOuterBrTags(deletetag(marker.contents, ['br']))
            .replace(/(<br>){3,}/g, '<br><br>');
}

// 마커생성, 맵 범위 조정, 표시
function createMarker(marker, tag, customHtmlContents = undefined) {
    const markerPosition = new kakao.maps.LatLng(marker.latitude, marker.longitude);
    marker.tag = tag;
    makeContents(marker);
    let content;
    if (customHtmlContents === undefined) {
        const title = marker.title.trim();
        if (title.endsWith("도서관") || marker.categoty === "도서관") {
            content = makeEmojiMarker(marker, "&#128214");
        } else if (title.endsWith("공원")) {
            content = makeEmojiMarker(marker, "&#127795");
        } else if (tag === "place") {
            content = makeCategoryInPlaceMarker(marker);
        } else {
            content = makeCategoryInEventMarker(marker);
        }
    } else {
        content = customHtmlContents;
    }

    const customOverlay = new kakao.maps.CustomOverlay({
        position: markerPosition,
        content: content,
        id: marker.id
    });

    customOverlay.setMap(map);
    bounds.extend(markerPosition);

    return customOverlay;
}

function makeCategoryInEventMarker(marker) {
    const strMarker = JSON.stringify(marker);
    return `<div type="button"`
        + `id=${marker.id} class='marker_category modal-link' marker='${strMarker}'>` +
        `<a href='javascript:void(0);' onclick='openModal(${strMarker});'>${marker.category}</a></div>`;
}

function makeCategoryInPlaceMarker(marker) {
    const strMarker = JSON.stringify(marker);
    return `<div type="button"`
        + `id=${marker.id} class='marker_place modal-link' marker='${strMarker}'>` +
        `<a href='javascript:void(0);' onclick='openModal(${strMarker});'>${marker.category}</a></div>`;
}

function makeEmojiMarker(marker, emoji) {
    const strMarker = JSON.stringify(marker);
    return `<div type="button"`
        + `id='${marker.id}' class='marker_emoji modal-link' marker='${strMarker}'>` +
        `<a href='javascript:void(0);' onclick='openModal(${strMarker});'>${emoji}</a></div>`;
}

function findMarkerAndOpen(id){
    openModal(markers[id]);
}

const widthThreshold = 700;
function openModal(marker) {
    if (marker.tag === "place"){
        const overlay = createMarker(marker, "place");
        registerOverlay(overlay, marker.id)

    } else if (marker.tag === "culture") {
        const overlay = createMarker(marker, "culture");
        registerOverlay(overlay, marker.id)
    }
    animateAtClicking(`#${marker.id}`, "hinge", "jackInTheBox");
    if (window.innerWidth > widthThreshold) {
        loadRightContainer(marker);
    } else {
        loadModal(marker);
    }
}


function showNoResultRightContent() {
    rightContainer.style.maxWidth = '30%';
    rightContainerEmpty.style.display = 'none';
    rightContainerContent.style.display = 'none';
    noResultRightContainer.style.display = "block";
}

function hideRightContent() {
    rightContainer.style.transition = "max-width 0s linear"
    rightContainer.style.maxWidth = '30%';
    rightContainerEmpty.style.display = 'block';
    noResultRightContainer.style.display = "none";
    rightContainerContent.style.display = 'none';
}

function showRightContent() {
    rightContainer.style.transition = "max-width 0.4s linear"
    rightContainer.style.maxWidth = '100%';
    rightContainerEmpty.style.display = 'none';
    noResultRightContainer.style.display = "none";
    rightContainerContent.style.display = 'block';
}