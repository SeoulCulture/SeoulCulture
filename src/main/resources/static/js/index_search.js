document.addEventListener('DOMContentLoaded', function() {
    // 뒤로가기시 display none 요소로 원상복구
    window.onpageshow = function(event) {
        if (event.persisted || window.performance.navigation.type === 2) {
            hideSearchingStatus();
        }
    }

    const submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        submitBtn.addEventListener('click', function() {
            showSearchingStatus();

            if(navigator.geolocation) {// geolocation 을 지원한다면 위치를 요청한다.
                navigator.geolocation.getCurrentPosition(success, error);
            }
            else
                console.log("이 브라우저에서는 Geolocation이 지원되지 않습니다.");
        });
    }

    function success(position) {
        const currentLatitude = position.coords.latitude.toString();
        const currentLongitude = position.coords.longitude.toString();
        console.log(currentLatitude, currentLongitude);
        localStorage.setItem("currentLatitude", currentLatitude);
        localStorage.setItem("currentLongitude", currentLongitude);

        let formData = new FormData(document.getElementById('form'));
        formData.append("latitude", currentLatitude);
        formData.append("longitude", currentLongitude);

        fetch('/culture/search', {
            method: 'GET',
            body: formData
        })
        .catch(error => {
            console.error('Error searching:', error);
        });
    }

    function error(e) {
        // 오류 객체에는 수치 코드와 텍스트 메시지가 존재한다.
        // 코드 값은 다음과 같다.
        // 1: 사용자가 위치 정보를 공유 권한을 제공하지 않음.
        // 2: 브라우저가 위치를 가져올 수 없음.
        // 3: 타임아웃이 발생됨.
        alert("Geolocation 오류 " + e.code + ": " + e.message);
    }

    function showSearchingStatus() {
        var searchStatus = document.getElementById('searchStatus');
        if (searchStatus) {
            searchStatus.style.display = 'block';
        }
    }

    function hideSearchingStatus() {
        var searchStatus = document.getElementById('searchStatus');
        if (searchStatus) {
            searchStatus.style.display = 'none';
        }
    }
});
