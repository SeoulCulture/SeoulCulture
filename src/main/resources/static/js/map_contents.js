function loadModal(marker) {
    setInformation(marker);
    $('#myModal').modal('show');
}

function loadRightContainer(marker) {
    showRightContent();
    setInformation(marker);
}

function relocationMarkers(){
    let preWidth = leftContainer.offsetWidth;
    const checkWidthInterval = setInterval(function() {
        const currentWidth = leftContainer.offsetWidth;
        if (currentWidth === preWidth) {
            clearInterval(checkWidthInterval);
            initMap();
        } else {
            preWidth = currentWidth;
        }
    }, 100);
}


function setInformation(marker) {
    console.log(marker)
    $(".place-category").text(marker.category);
    $(".place-title").text(marker.title);
    const placeImg = document.querySelectorAll('.place-img img');
    placeImg.forEach(function(img) {
        img.src = marker.imgUrl;
    });
    $(".place-contents").html(marker.contents);
    const placeDetail = document.querySelectorAll('.btnPlaceDetail');
    placeDetail.forEach(function(button) {
        button.setAttribute('onclick', `openDetailUrl('${marker.detailUrl}')`);
    });

    const paths = document.querySelectorAll('.btnPathCopy');
    paths.forEach(function(button) {
            button.setAttribute('onclick', `openPath('${marker.latitude}', '${marker.longitude}'); toastOn()`);
    });
}

function openPath(dstLat, dstLon){

    const baseurl = '/culture/address';

    const howToGo = new URL(location.href).searchParams.get('howToGo');

    const params = {
        srcLat : currentLatitude,
        srcLon : currentLongitude,
        dstLat : dstLat,
        dstLon : dstLon,
        howToGo : howToGo
    };

    const queryString = new URLSearchParams(params).toString();  // url에 쓰기 적합한 querySting으로 return 해준다.
    const requrl = `${baseurl}?${queryString}`;   // 완성된 요청 url.

    console.log(requrl);

    fetch(requrl)
      .then(response => {
        return response.text();
      })
      .then(data => {
          // 이 data로 길찾기를 시작하면 됨.
          console.log(data)

          navigator.clipboard.writeText(data)
              .then(() => {
              console.log("Text copied to clipboard...")
          })
              .catch(err => {
              console.log('Something went wrong', err);
          })
    });

}

//1. 토스트 메시지, 버튼요소를 변수에 대입
let toastMessage = document.getElementById('toast_message');

//2. 토스트 메시지 노출-사라짐 함수 작성
function toastOn(){
    toastMessage.classList.add('active');
    setTimeout(function(){
        toastMessage.classList.remove('active');
    },1000);
}

function openDetailUrl(url){
    if(!checkUrlValidate(url)){
        alert('홈페이지 주소가 존재하지 않습니다. 이용에 불편을 드려 죄송합니다.')
    }else{
        window.open(url)
    }
}

function checkUrlValidate(url){
    try {
        new URL(url);
        return true;
    } catch (err) {
        return false;
    }
}


window.addEventListener('resize', function() {
    // relocationMarkers();
});
