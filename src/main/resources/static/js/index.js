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