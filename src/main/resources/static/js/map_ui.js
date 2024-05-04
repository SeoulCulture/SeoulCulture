let map;
let bounds;
let mapElement = document.getElementById('map');
console.log(currentLatitude, currentLongitude);
const rightContainer = document.getElementById('rightContainer');
const leftContainer = document.getElementById('leftContainer');
const rightContainerContent = document.getElementById('rightContainerContent');
const rightContainerEmpty = document.getElementById('rightContainerEmpty');

window.onload = function () {
    hideRightContent();
    initMap();
}

$(document).ready(function(){
    var elem = $('.animated');

    elem.each(function () {
        var isAnimate = $(this).data('animate');
        $(this).addClass(isAnimate);
    });
});


// 함수 =============================================================================================
function initMap() {
    mapElement = document.getElementById('map');
    var options = {center: new kakao.maps.LatLng(currentLatitude, currentLongitude)};
    map = new kakao.maps.Map(mapElement, options);
    initBounds();
    initMarkers();
    initCenter();
    setBounds();
}

function initBounds() {
    bounds = new kakao.maps.LatLngBounds();
}

function initCenter() {
    const currentLocationMarker = {
        "latitude": currentLatitude,
        "longitude": currentLongitude
    };
    createMarker(currentLocationMarker, "home", '<div class="lightPoint"></div><div><div class="marker_home animated">' +
        '<div class="face" data-animate="rubberBand">' +
        '    <div class="eyes">' +
        '        <div class="eye"></div>' +
        '        <div class="eye"></div>' +
        '        <div class="tongue"></div>'+
        '    </div>' +
        '</div><div class="arm arm-left"></div><div class="arm arm-right"></div><div class="marker_shadow">' +
        '</div></div>');
}

function initMarkers() {
    let index = 0;
    placeInfo.forEach(function (marker) {
        marker.id = ++index;
        createMarker(marker, "place")
    });
    markerInfo.forEach(function (marker) {
        marker.id = ++index;
        createMarker(marker, "culture");
    });
}

function setBounds() {
    map.setBounds(bounds);
}

// 마커생성, 맵 범위 조정, 표시
function createMarker(marker, tag, customHtmlContents = undefined) {
    const markerPosition = new kakao.maps.LatLng(marker.latitude, marker.longitude);

    let content;
    if (customHtmlContents === undefined) {
        if (marker.title.endsWith("도서관") || marker.categoty === "도서관"){
            content = makeEmojiMarker(marker, "&#128214");
        } else if (marker.title.endsWith("공원")){
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
        content: content
    });

    customOverlay.setMap(map);
    bounds.extend(markerPosition);
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
    let findMarker = document.getElementById(id).getAttribute('marker');
    openModal(JSON.parse(findMarker))
}

const widthThreshold = 700;
function openModal(marker) {
    if (window.innerWidth > widthThreshold) {
        loadRightContainer(marker);
    } else {
        loadModal(marker);
    }
}

function hideRightContent() {
    rightContainerEmpty.style.display = 'block';
    rightContainerContent.style.display = 'none';
}

function showRightContent() {
    rightContainerEmpty.style.display = 'none';
    rightContainerContent.style.display = 'block';
}
