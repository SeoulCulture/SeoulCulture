let spotDtos;

document.addEventListener("DOMContentLoaded", function() {
  const apiUrl = '/moods';
  fetch(apiUrl)
    .then(response => {
      return response.json();
    })
    .then(data => {
      const moodList = data.mood;

      initializeTagify(moodList);
    })
    .catch(error => {
      console.error('[ERROR] moods 가져오기 에러: ', error);
  });

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

  const freeCheckbox = document.getElementById('free');
  // '무료' 체크박스 관련 비용입력 비활성화 처리
  freeCheckbox.addEventListener('change', function () {
    const freeCheckbox = document.getElementById('free');
    const priceInput = document.getElementById('price');
    if (freeCheckbox.checked) {
      priceInput.disabled = true;
      priceInput.value = null;
    } else {
      priceInput.disabled = false;
      priceInput.focus();
    }
  });
});

// moods 를 읽어오는 API 요청 응답 처리
function initializeTagify(moodList) {
  const input = document.querySelector('input[name=moods]');

  console.log('Received mood list:', moodList);

  let tagify = new Tagify(input, {
    whitelist: moodList,
    userInput: false // 입력 제한
  });
}
``
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