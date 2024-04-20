function loadModal(marker) {
    setInformation(marker);
    $('#myModal').modal('show');
}

function loadRightContainer(marker) {
    showRightContent();
    relocationMarkers();
    rightContainer.style.maxWidth = '100%';
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
    $(".place-category").text(marker.category);
    $(".place-title").text(marker.title);
    const placeImg = document.querySelectorAll('.place-img img');
    placeImg.forEach(function(img) {
        img.src = marker.imgUrl;
    });
    $(".place-contents").text("내용: " + marker.contents);
    $(".place-price").text("가격: " + marker.price);
    const placeDetail = document.querySelectorAll('.place-detail');
    placeDetail.forEach(function(button) {
        // http 붙어서 데이터 들어올 수도 있으니까 처리해야 함
        button.setAttribute('onclick', `window.open('http://${marker.detailUrl}')`);
    });

}