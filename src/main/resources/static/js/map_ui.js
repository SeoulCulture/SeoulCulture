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

    kakao.maps.event.addListener(map, 'dragend', function () {
        animateMarkers();
    });
    kakao.maps.event.addListener(map, 'zoom_changed', function () {
        animateMarkers();
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
function initMap() {
    mapElement = document.getElementById('map');
    var options = {center: new kakao.maps.LatLng(currentLatitude, currentLongitude)};
    map = new kakao.maps.Map(mapElement, options);
    initBounds();
    initMarkers();
    setBounds();
    animateMarkers();
    initCenter();
}

function initBounds() {
    bounds = new kakao.maps.LatLngBounds();
}

function initCenter() {
    const currentLocationMarker = {
        "latitude": currentLatitude,
        "longitude": currentLongitude
    };
    createMarker(currentLocationMarker, "home", '<div>' +
        '<div class="lightPoint animate__animated animate__heartBeat animate__infinite animate__slower"></div>'+
        '<div class="marker_home">' +
        '<div class="face">' +
        '    <div class="eyes">' +
        '        <div class="eye"></div>' +
        '        <div class="eye"></div>' +
        '    </div>' +
        '    <div class="tongue"></div>'+
        '</div>' +
        '<div class="arm arm-left"></div><div class="arm arm-right"></div><div class="marker_shadow">' +
        '</div></div>'
    );

    animateMarkers();
}

const markerInList = [];

function initMarkers() {
    placeInfo.forEach(function (marker) {
        createMarker(marker, "place")
    });
    markerInfo.forEach(function (marker) {
        markerInList.push(marker);
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
    createMarker(marker, 'culture');
    animateAtClicking(`#${marker.id}`, "hinge", "jackInTheBox");
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

const animateCSS = (element, animation, speed_="", prefix = 'animate__') =>
    // We create a Promise and return it
    new Promise((resolve, reject) => {
        const animationName = `${prefix}${animation}`;
        const speed = `${prefix}${speed_}`;
        const nodes = document.querySelectorAll(element);

        nodes.forEach((node) => {
            node.classList.add(`${prefix}animated`, animationName);
            if (speed.length != 0)
                node.classList.add(speed);
            // When the animation ends, we clean the classes and resolve the Promise
            function handleAnimationEnd(event) {
                event.stopPropagation();
                node.classList.remove(`${prefix}animated`, animationName);
                if (speed.length != 0)
                    node.classList.remove(speed);
            }
            node.addEventListener('animationend', handleAnimationEnd, { once: true });
        });
});


const animateAtClicking = (element, animation1, animation2, speed_="", prefix = 'animate__') =>
    // We create a Promise and return it
    new Promise((resolve, reject) => {
        const animationName1 = `${prefix}${animation1}`;
        const speed = `${prefix}${speed_}`;
        const nodes = document.querySelectorAll(element);
        nodes.forEach((node) => {
            node.classList.add(`${prefix}animated`, animationName1);
            if (speed.length != 0)
                node.classList.add(speed);
            function handleAnimationEnd(event) {
                event.stopPropagation();
                node.classList.remove(`${prefix}animated`, animationName1);
                if (speed.length != 0)
                    node.classList.remove(speed);
                animateCSS(element, animation2, "faster")
            }
            node.addEventListener('animationend', handleAnimationEnd, { once: true });
        });
    });

function animateMarkers() {
    jumpingHomeMarker();
    animateCSS('.marker_place', 'bounceIn', "faster");
    animateCSS('.marker_emoji', 'bounceIn');
    animateCSS('.marker_category', 'pulse');
}
function jumpingHomeMarker() {
    animateCSS('.face', 'bounce');
    animateCSS('.arm', 'bounce');
}

function getRandomAnimation() {
    const animations = ["rubberBand", "wobble", "flip"];
    const randomIndex = Math.floor(Math.random() * animations.length);
    return animations[randomIndex];
}