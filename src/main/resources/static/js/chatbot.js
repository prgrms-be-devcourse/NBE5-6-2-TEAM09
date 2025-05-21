function toggleChatbot() {
  const modal = document.getElementById('chatbot-modal');
  modal.style.display = modal.style.display === 'none' ? 'block' : 'none';
}

function sendKeyword(keyword) {
  document.getElementById('chatbot-response').innerText = '답변을 불러오는 중...';

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
