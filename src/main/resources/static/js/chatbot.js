function toggleChatbot() {
  const modal = document.getElementById('chatbot-modal');
  if (modal.classList.contains('show')) {
    modal.classList.remove('show');
    setTimeout(() => {
      modal.style.display = 'none';
      modal.style.pointerEvents = 'none';
    }, 300);
  } else {
    modal.style.display = 'flex';
    setTimeout(() => {
      modal.classList.add('show');
      modal.style.pointerEvents = 'auto';
    }, 10);
  }
}

// ESC 키로 닫기
document.addEventListener('keydown', function (event) {
  if (event.key === 'Escape') {
    const modal = document.getElementById('chatbot-modal');
    if (modal.classList.contains('show')) {
      toggleChatbot();
    }
  }
});

// 외부 클릭 시 닫기
document.addEventListener('click', function (event) {
  const modal = document.getElementById('chatbot-modal');
  const wrapper = document.getElementById('chatbot-modal-wrapper');
  const button = document.getElementById('chatbot-button');
  if (modal.classList.contains('show') &&
      !modal.contains(event.target) &&
      !button.contains(event.target)) {
    toggleChatbot();
  }
});

// 입력창에서 키워드 전송
function sendKeywordFromInput() {
  const input = document.getElementById('chatbot-input');
  const keyword = input.value.trim();
  if (keyword) {
    sendKeyword(keyword);
    input.value = '';
  }
}

// 실제 키워드 전송 로직
function sendKeyword(keyword) {
  document.getElementById('chatbot-response').innerText = '답변을 불러오는 중...';
  localStorage.setItem('lastKeyword', keyword);

  fetch('/chatbot/message', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ keyword })
  })
  .then(response => response.json())
  .then(data => {
    document.getElementById('chatbot-response').innerText = data.response;
  })
  .catch(error => {
    document.getElementById('chatbot-response').innerText = '오류가 발생했어요 😥';
    console.error('Error:', error);
  });
}

// 마지막 키워드 복원
window.addEventListener('DOMContentLoaded', () => {
  const last = localStorage.getItem('lastKeyword');
  if (last) {
    sendKeyword(last);
  }
});
