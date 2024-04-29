// moods 를 읽어오는 API 요청 응답 처리
function initializeTagify(moodList) {
  const input = document.querySelector('input[name=moods]');

  console.log('Received mood list:', moodList);

  let tagify = new Tagify(input, {
    whitelist: moodList,
    userInput: false // 입력 제한
  });
}

window.onload = function ()
{
  const apiUrl = '/moods';
  fetch(apiUrl)
    .then(response => {
      // 응답을 JSON 형식으로 파싱
      return response.json();
    })
    .then(data => {
      // 응답 데이터(data)를 처리하여 변수에 저장
      const moodList = data.mood; // 여기서 data는 List<String>으로 가정합니다.

      initializeTagify(moodList);
    })
    .catch(error => {
      console.error('Error fetching moods:', error);
  });

// '무료' 체크박스 관련 비용입력 비활성화 처리
  var freeCheckbox = document.getElementById('free');
  var priceInput = document.getElementById('price');

  freeCheckbox.addEventListener('change', function () {
    if (freeCheckbox.checked) {
      priceInput.disabled = true;
      priceInput.value = null;
    } else {
      priceInput.disabled = false;
      priceInput.focus();
    }
  });
}