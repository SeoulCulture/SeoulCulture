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

            // submitBtn 클릭 시에는 이미 formData에 위도와 경도 값이 설정되어 있음
            sendSearchRequest();
        });
    }

    function sendSearchRequest() {
        showSearchingStatus();
        let formData = new FormData(document.getElementById('form'));

        // 위치 정보를 포함한 formData를 서버에 전송
        fetch('/culture/search', {
            method: 'POST',
            body: formData
        })
        .then(response => {
        })
        .catch(error => {
            console.error('Error searching:', error);
        });
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
