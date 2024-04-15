document.addEventListener('DOMContentLoaded', function() {
    // 뒤로가기시 display none 요소로 원상복구
    window.onpageshow = function(event) {
        if (event.persisted || window.performance.navigation.type === 2) {
            hideSearchingStatus();
        }
    }

    var submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        submitBtn.addEventListener('click', function() {
            showSearchingStatus();

            var formData = new FormData(document.getElementById('form'));

            fetch('/culture/search', {
                method: 'GET',
                body: formData
            })
            .then(response => {
                // 응답 받고 아무 것도 안 할거다. 어차피 map.html로 이동됨!
                console.log('Received data:', data);
            })
            .catch(error => {
                console.error('Error searching:', error);
            })
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
});
