let confusions = {};

document.addEventListener("DOMContentLoaded", function() {

    fetch('/confusion')
        .then(response => {
            return response.json();
        })
        .then(data => {
            console.log("[혼잡영역] 받아옴: " + data.length);
            for (idx in data) {
                const confusionData = data[idx];
                drawAreaInMap(confusionData);
            }
            // 의아한 것: 위에서 폴리곤이 안 그려져서 아래id로 가져온 요소가 null인 경우가 종종 있음
            for (let i = 0; i < data.length; i++) {
                let element = document.getElementById(`daum-maps-shape-${i}`);
                if (element) {
                    console.log("폴리곤 잘 뜸");
                } else {
                    console.log("폴리곤 에러");
                }
            }
        });

});
// 혼잡도에 따른 영역 시각화 (다각형 그리기)
function drawAreaInMap(confusionData) {
    const areas = confusionData['areas'];
    const detail = confusionData['confusionData'];
    const confusion = {
        "nameKor" : confusionData['name'],
        "nameEng" : confusionData['eng'],
        "confusion" : detail['confusion'],
        "msg" : detail['confusionMsg'],
        'updateTime' : detail['updateTime']
    }
    confusions[confusionData['poi']] = confusion;  // 전역에 저장

    if(areas.length > 3){
        let polygonPath = createPolygonPath(areas);
        const polygon = drawPolygon(polygonPath, detail['confusion'])
        polygon.setMap(map);
        addEventByConfusionTag(detail['confusion'], polygon, confusion);
    }
    else if (confusionData['areas'].length == 1) {
        let [lat, lon] = areas[0].split(",");
        const circle = drawCircle(lat, lon, detail['confusion']);
        addEventByConfusionTag(detail['confusion'], circle, confusion);
    }
}

function drawCircle(lat, lon, confusionTag) {
    let colorCode = setColorcodeByConfusion(confusionTag);
    var circle = new kakao.maps.Circle({
        center: new kakao.maps.LatLng(parseFloat(lat), parseFloat(lon)),
        radius: 100, // meter
        strokeWeight: 3,
        strokeColor: colorCode,
        strokeOpacity: 0.8,
        strokeStyle: 'dashed',
        fillColor: colorCode,
        fillOpacity: 0.25
    });
    circle.setMap(map);
    return circle;
}

function drawPolygon(polygonPath, confusionTag) {
    let colorCode;
    colorCode = setColorcodeByConfusion(confusionTag);
    const polygon = new kakao.maps.Polygon({
        path: polygonPath, // 그려질 다각형의 좌표 배열입니다
        strokeWeight: 3, // 선의 두께입니다
        strokeColor: colorCode, // 선의 색깔입니다
        strokeOpacity: 0.8, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: 'dashed', // 선의 스타일입니다
        fillColor: colorCode, // 채우기 색깔입니다
        fillOpacity: 0.25 // 채우기 불투명도 입니다
    });
    return polygon;
}

let confusionToast = document.getElementById('confusionToast');
function overConfuse1(confusion){
    confusionToast.innerHTML = makeConfusionMsgString(confusion);
    confusionToastOn();
}
function overConfuse2(confusion){
    confusionToast.innerHTML = makeConfusionMsgString(confusion);
    confusionToastOn();
}
function overConfuse3(confusion){
    confusionToast.innerHTML = makeConfusionMsgString(confusion);
    confusionToastOn();
}
function overConfuse4(confusion){
    confusionToast.innerHTML = makeConfusionMsgString(confusion);
    confusionToastOn();
}
function makeConfusionMsgString(confusion){
    let color;
   switch (confusion['confusion']) {
     case '붐빔':
       color = '🔴'
       break;
     case '약간 붐빔':
       color = '🟡'
       break;
     case '보통':
       color = '🟢'
       break;
     case '여유':
       color = '🔵'
       break;
    }
    return `${color} ${confusion['nameKor']} 혼잡도 : [${confusion['confusion']}]`
}
function outConfuse1() {
    confusionToast.classList.remove('active');
}
function outConfuse2() {
    confusionToast.classList.remove('active');
}
function outConfuse3() {
    confusionToast.classList.remove('active');
}
function outConfuse4() {
    confusionToast.classList.remove('active');
}
function confusionToastOn(){
    confusionToast.classList.add('active');
}

function createPolygonPath(dataList) {
    let polygonPath = [];
    dataList.forEach(data => {
        let [lat, lon] = data.split(",");
        polygonPath.push(new kakao.maps.LatLng(parseFloat(lat), parseFloat(lon)));
    });
    return polygonPath;
}


function setColorcodeByConfusion(confusionTag, colorCode) {
    if (confusionTag == "여유") {
        colorCode = 'rgb(113,164,255)';
    } else if (confusionTag == "보통") {
        colorCode = '#4ac000';
    } else if (confusionTag == "약간 붐빔") {
        colorCode = '#ffdd44';
    } else if (confusionTag == "붐빔") {
        colorCode = '#ff2626';
    }
    return colorCode;
}

function addEventByConfusionTag(confusionTag, areaObject, confusion) {
    // 마우스오버 이벤트
    if (confusionTag == "여유") {
        kakao.maps.event.addListener(areaObject, 'mouseover', function () {
            overConfuse1(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'touchstart', function () {
            overConfuse1(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'mouseout', function () {
            outConfuse1();
        });
        kakao.maps.event.addListener(areaObject, 'touchend', function () {
            outConfuse1();
        });
    } else if (confusionTag == "보통") {
        kakao.maps.event.addListener(areaObject, 'mouseover', function () {
            overConfuse2(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'touchstart', function () {
            overConfuse2(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'mouseout', function () {
            outConfuse2();
        });
        kakao.maps.event.addListener(areaObject, 'touchend', function () {
            outConfuse2();
        });
    } else if (confusionTag == "약간 붐빔") {
        kakao.maps.event.addListener(areaObject, 'mouseover', function () {
            overConfuse3(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'touchstart', function () {
            overConfuse3(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'mouseout', function () {
            outConfuse3();
        });
        kakao.maps.event.addListener(areaObject, 'touchend', function () {
            outConfuse3();
        });
    } else if (confusionTag == "붐빔") {
        kakao.maps.event.addListener(areaObject, 'mouseover', function () {
            overConfuse4(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'touchstart', function () {
            overConfuse4(confusion);
        });
        kakao.maps.event.addListener(areaObject, 'mouseout', function () {
            outConfuse4();
        });
        kakao.maps.event.addListener(areaObject, 'touchend', function () {
            outConfuse4();
        });
    }
}
