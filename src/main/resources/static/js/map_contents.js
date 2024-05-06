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
    $(".place-price").html("가격: " + marker.price);
    const placeDetail = document.querySelectorAll('.place-detail');
    placeDetail.forEach(function(button) {
        button.setAttribute('onclick', `openDetailUrl('${marker.detailUrl}')`);
    });
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
