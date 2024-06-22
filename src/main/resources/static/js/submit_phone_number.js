document.addEventListener('DOMContentLoaded', function () {
    const submitButton = document.getElementById('subs-submit-button');

    submitButton.addEventListener('click', function () {
        const phoneInput = document.getElementById('phone').value;
        const keywordInput = document.getElementById('keyword').value;

        if (phoneInput && keywordInput) {
            const requestData = {
                phoneNumber: phoneInput,
                message: keywordInput
            };

            fetch('/subscribe', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
                .then(response => {
                    if (response.ok) {
                        alert('알림 신청이 성공적으로 완료되었습니다!');
                        // 모달 닫기
                        document.getElementById('subs-modal').style.display = 'none';
                    } else {
                        alert('알림 신청에 실패했습니다. 다시 시도해주세요.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('알림 신청 중 오류가 발생했습니다.');
                });
        } else {
            alert('휴대폰 번호와 행사를 입력해주세요.');
        }
    });

    // 모달 닫기 기능
    const modal = document.getElementById('subs-modal');
    const closeButton = document.querySelector('.close');
    closeButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });
});