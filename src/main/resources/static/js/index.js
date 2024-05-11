let spotDtos;

document.addEventListener("DOMContentLoaded", function() {
  showParagraph();

  const spotUrl = '/spots';
  fetch(spotUrl)
      .then(response => {
        return response.json();
      })
      .then(data => {
        spotDtos = data;
        console.log("가져온 spotDtos 데이터 개수: ", spotDtos.length);
      })
      .catch(error => {
        console.error('[ERROR] spotDtos 가져오기 에러: ', error);
  });
});

function togglePriceBox(checkbox) {
  // 1. 텍스트 박스 element 찾기
  const priceInput = document.getElementById('price');

  // 2-1. 체크박스 선택여부 체크
  // 2-2. 체크박스 선택여부에 따라 텍스트박스 활성화/비활성화
  priceInput.disabled = checkbox.checked ? true : false;

  // 3. 텍스트박스 활성화/비활성화 여부에 따라
  // - 텍스트박스가 비활성화 된 경우 : 텍스트박스 초기화
  // - 텍스트박스가 활성화 된 경우 : 포커스 이동
  if(priceInput.disabled) {
    priceInput.value = null;
  }else {
    priceInput.focus();
  }
}

function toggleLocationBox(checkbox){
  // 1. 텍스트 박스 element 찾기
  const locationInput = document.getElementById('searchInput');

  // 2-1. 체크박스 선택여부 체크
  // 2-2. 체크박스 선택여부에 따라 텍스트박스 활성화/비활성화
  locationInput.disabled = checkbox.checked ? true : false;

  // 3. 텍스트박스 활성화/비활성화 여부에 따라
  // - 텍스트박스가 비활성화 된 경우 : 텍스트박스 초기화
  // - 텍스트박스가 활성화 된 경우 : 포커스 이동
  if(locationInput.disabled) {
    locationInput.value = null;
  }else {
    locationInput.focus();
  }
}


function showParagraph() {
  var paragraphs = ["paragraph1", "paragraph2", "paragraph3"];
  var index = 0;

  document.getElementById(paragraphs[index]).style.opacity = "1";

  var interval = setInterval(function() {
    // 다음 단락 표시
    index++;
    if (index >= paragraphs.length) {
      clearInterval(interval); // 마지막 단락까지 표시되면 인터벌 정지
    } else {
      document.getElementById(paragraphs[index]).style.opacity = "1";
    }
  }, 1000);
}