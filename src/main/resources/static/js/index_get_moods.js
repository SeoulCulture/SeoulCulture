// API 요청을 보낼 URL
const apiUrl = '/moods';

// API 요청 보내기
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
// 오류 처리
console.error('Error fetching moods:', error);
});

function initializeTagify(moodList) {
  const input = document.querySelector('input[name=moods]');

  console.log('Received mood list:', moodList);

  let tagify = new Tagify(input, {
    whitelist: moodList,
    userInput: false // 입력 제한
  });
}
