document.addEventListener('DOMContentLoaded', function() {
    // 뒤로가기시 display none 요소로 원상복구
    window.onpageshow = function(event) {
        if (event.persisted || window.performance.navigation.type === 2) {
            closeLoadingWithMask();
        }
    }

    function closeLoadingWithMask() {
        $('#mask, #loadingImg').hide();
        $('#mask, #loadingImg').empty();
     }

    const form = document.getElementById('form');
    form.onsubmit = function (event) {
        //화면의 높이와 너비를 구합니다.
        var maskHeight = $(document).height();
        var maskWidth = window.document.body.clientWidth;

//        화면에 출력할 마스크를 설정해줍니다.
        var mask = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
        var loadingImg = " <img id='loadingImg' src='https://tistory2.daumcdn.net/tistory/1898109/skin/images/Spinner.gif' style='position: absolute; display: block; margin: 0px auto;'/>";

//        화면에 레이어 추가
        $('body')
            .append(mask);

        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
        $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' : '0.7',
            'display' : 'flex',
            'justify-content' : 'center',
            'align-items' : 'center'
        });

        //마스크 표시
        $('#mask').show();

        //로딩중 이미지 표시
        $('#mask').append(loadingImg);
        $('#loadingImg').show();
    }

    function LoadingWithMask(gif) {

    }

    // 페이지 로드 시에 formData에 위도와 경도 값을 설정
    window.addEventListener('load', function() {
        if (navigator.geolocation) {
            // geolocation을 지원하면 위치를 요청
            navigator.geolocation.getCurrentPosition(success, error);
        } else {
            console.log("이 브라우저에서는 Geolocation이 지원되지 않습니다.");
        }
    });

    function success(position) {
        const currentLatitude = position.coords.latitude.toString();
        const currentLongitude = position.coords.longitude.toString();
        console.log(currentLatitude, currentLongitude);
        localStorage.setItem("currentLatitude", currentLatitude);
        localStorage.setItem("currentLongitude", currentLongitude);

        // 기존 form 요소에 직접 접근하여 위도와 경도를 추가
        let form = document.getElementById('form');
        form.innerHTML += `<input type="hidden" name="latitude" value="${currentLatitude}">`;
        form.innerHTML += `<input type="hidden" name="longitude" value="${currentLongitude}">`;
    }

    function error(e) {
        // 위치 정보를 가져오는 데 실패한 경우
        alert("Geolocation 오류 " + e.code + ": " + e.message);
    }
});
