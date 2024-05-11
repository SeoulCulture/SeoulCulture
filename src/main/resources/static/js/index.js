let spotDtos;

document.addEventListener("DOMContentLoaded", function() {
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

function toggleLocationBox(checkbox){
  const locationInput = document.getElementById('searchInput');

  locationInput.disabled = checkbox.checked ? true : false;

  if(locationInput.disabled) {
    locationInput.value = null;
  }else {
    locationInput.focus();
  }
}