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
        let polygonPath = createPolygonPath(areas);
        // 영역 시각화 (다각형 그리기)
        var polygon = new kakao.maps.Polygon({
            path:polygonPath, // 그려질 다각형의 좌표 배열입니다
            strokeWeight: 3, // 선의 두께입니다
            strokeColor: '#39DE2A', // 선의 색깔입니다
            strokeOpacity: 0.8, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
            strokeStyle: 'longdash', // 선의 스타일입니다
            fillColor: '#A2FF99', // 채우기 색깔입니다
            fillOpacity: 0.7 // 채우기 불투명도 입니다
        });
        polygon.setMap(map);
    }
    else if (confusionData['areas'].length == 1) {
        // 일정 반경의 원?
    }
}

function createPolygonPath(dataList) {
    let polygonPath = [];
    dataList.forEach(data => {
        let [lat, lon] = data.split(",");
        polygonPath.push(new kakao.maps.LatLng(parseFloat(lat), parseFloat(lon)));
    });
    return polygonPath;
}