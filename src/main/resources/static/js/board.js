/**
 *  커뮤니티 보드용~~
 */

setSessionStorage();

//============================= 사용자 데이터 설정
function setSessionStorage(){
	// 세션 전역변수 -> HttpSession(서버) 에서 SessionStorage(클라이언트)로 데이터 저장
	const query = `/api/setSessionStorage`;
	fetch(query)
	    .then(response => response.text())
	    .then(data => {
	        console.log(data);
	        const userIdInputTag = document.getElementById("userIdInputTag");
            userIdInputTag.value = data;
	    })
	    .catch(error => console.error("Error:", error));
}