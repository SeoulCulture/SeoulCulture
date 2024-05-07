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
    if(areas.length > 3){
        console.log("[혼잡영역] 그리기 시작");
        // 현재 confusion을 전역에 저장
        const detail = confusionData['confusionData'];
        const confusion = {
            "nameKor" : confusionData['name'],
            "nameEng" : confusionData['eng'],
            "confusion" : detail['confusion'],
            "msg" : detail['confusionMsg'],
            "maleRate" : detail['maleRate'],
            'femaleRate' : detail['femaleRate'],
            'rate0': detail['rateIn0To10'],
            'rate10' : detail['rateIn10'],
            'rate20' : detail['rateIn20'],
            'rate30' : detail['rateIn30'],
            'rate40' : detail['rateIn40'],
            'rate50' : detail['rateIn50'],
            'rate60' : detail['rateIn60'],
            'rate70' : detail['rateIn70'],
            'resentRate' : detail['resentRate'],
            'nonResentRate' : detail['nonResentRate'],
            'updateTime' : detail['updateTime']
        }
        confusions[confusionData['poi']] = confusion;

        let polygonPath = createPolygonPath(areas);
        const polygon = drawPolygon(polygonPath, detail['confusion'])
        polygon.setMap(map);
        console.log("[혼잡영역] 그렸음");
        addEventByConfusionTag(detail['confusion'], polygon, confusion);
    }
    else if (confusionData['areas'].length == 1) {
        // 일정 반경의 원?
    }
}

function drawPolygon(polygonPath, confusionTag) {
    // 여유 50% 이하 인구가 평소와 비교하여 적음
    // 보통 50% 초과 75% 이하 인구가 평소와 비교하여 비슷함
    // 약간 붐빔 75% 초과 100% 이하 인구가 평소와 비교하여 많음
    // 붐빔 100% 초과 인구가 평소와 비교하여 매우 많음
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
    return `[${confusion['confusion']}] ${confusion['nameKor']}`
    // "nameKor" : confusionData['name'],
    //     "nameEng" : confusionData['eng'],
    //     "msg" : detail['confusionMsg'],
    //     "maleRate" : detail['maleRate'],
    //     'femaleRate' : detail['femaleRate'],
    //     'rate0': detail['rateIn0To10'],
    //     'rate10' : detail['rateIn10'],
    //     'rate20' : detail['rateIn20'],
    //     'rate30' : detail['rateIn30'],
    //     'rate40' : detail['rateIn40'],
    //     'rate50' : detail['rateIn50'],
    //     'rate60' : detail['rateIn60'],
    //     'rate70' : detail['rateIn70'],
    //     'resentRate' : detail['resentRate'],
    //     'nonResentRate' : detail['nonResentRate'],
    //     'updateTime' : detail['updateTime']
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

function addEventByConfusionTag(confusionTag, polygon, confusion) {
    // 마우스오버 이벤트
    if (confusionTag == "여유") {
        kakao.maps.event.addListener(polygon, 'mouseover', function () {
            overConfuse1(confusion);
        });
        kakao.maps.event.addListener(polygon, 'mouseout', function () {
            outConfuse1();
        });
    } else if (confusionTag == "보통") {
        kakao.maps.event.addListener(polygon, 'mouseover', function () {
            overConfuse2(confusion);
        });
        kakao.maps.event.addListener(polygon, 'mouseout', function () {
            outConfuse2();
        });
    } else if (confusionTag == "약간 붐빔") {
        kakao.maps.event.addListener(polygon, 'mouseover', function () {
            overConfuse3(confusion);
        });
        kakao.maps.event.addListener(polygon, 'mouseout', function () {
            outConfuse3();
        });
    } else if (confusionTag == "붐빔") {
        kakao.maps.event.addListener(polygon, 'mouseover', function () {
            overConfuse4(confusion);
        });
        kakao.maps.event.addListener(polygon, 'mouseout', function () {
            outConfuse4();
        });
    }
}
