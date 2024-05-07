let confusions = {};

fetch('/confusion')
    .then(response => {
        return response.json();
    })
    .then(data => {
        for (idx in data) {
            const confusionData = data[idx];
            drawAreaInMap(confusionData);
        }
    });

function drawAreaInMap(confusionData) {
    const areas = confusionData['areas'];
    if(areas.length > 3){
        // 현재 confusion을 전역에 저장
        const detail = confusionData['confusionData'];
        const confusion = {
            "nameKor" : confusionData['name'],
            "nameEng" : confusionData['eng'],
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

        // 혼잡도에 따른 영역 시각화 (다각형 그리기)
        let polygonPath = createPolygonPath(areas);
        drawPolygon(polygonPath, detail['confusion'])
    }
    else if (confusionData['areas'].length == 1) {
        // 일정 반경의 원?
    }
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
    polygon.setMap(map);

    // 마우스오버 이벤트
    if (confusionTag == "여유") {
        kakao.maps.event.addListener(polygon, 'mouseover', function() {
            confuse1();
        });
    } else if (confusionTag == "보통") {
        kakao.maps.event.addListener(polygon, 'mouseover', function() {
            confuse2();
        });
    } else if (confusionTag == "약간 붐빔") {
        kakao.maps.event.addListener(polygon, 'mouseover', function() {
            confuse3();
        });
    } else if (confusionTag == "붐빔") {
        kakao.maps.event.addListener(polygon, 'mouseover', function() {
            confuse4();
        });
    }

    return polygon;
}

function confuse1(){
    alert("원활");
}


function confuse2(){
    alert("보통");
}

function confuse3(){
    alert("붐빔");
}

function confuse4(){
    alert("매우붐빔");
}

function createPolygonPath(dataList) {
    let polygonPath = [];
    dataList.forEach(data => {
        let [lat, lon] = data.split(",");
        polygonPath.push(new kakao.maps.LatLng(parseFloat(lat), parseFloat(lon)));
    });
    return polygonPath;
}