// 현재 연도와 월을 전역 변수로 관리 [수정됨]
let currentYear = new Date().getFullYear(); // [수정됨]
let currentMonth = new Date().getMonth(); // 0부터 시작 (0: 1월, 11: 12월) [수정됨]

// 달력 생성 함수
function loadCalendar() {
    const calendarTitle = document.getElementById("calendar-title");
    const calendarDays = document.getElementById("calendar-days");
    // onst currentDate = new Date();
    // const year = currentDate.getFullYear();
    // const month = currentDate.getMonth();

    // 달력 제목 설정
    calendarTitle.innerText = `${currentYear}년 ${currentMonth + 1}월`; // [수정됨]

    // 달력 초기화
    calendarDays.innerHTML = '';

    // 월의 첫 번째 날과 마지막 날 계산
	const firstDay = new Date(currentYear, currentMonth, 1).getDay(); // [수정됨]
    const lastDate = new Date(currentYear, currentMonth + 1, 0).getDate(); // [수정됨]

    // 오늘 날짜 구하기
    const today = new Date();
    const todayYear = today.getFullYear();
    const todayMonth = today.getMonth();
    const todayDate = today.getDate();

    // 새로고침 시 오늘 날짜를 자동 선택
    if (selectedDate === null) {
        selectedDate = new Date(todayYear, todayMonth, todayDate).toISOString().split('T')[0];
    }

    // 빈 칸 추가 (첫 주의 시작 요일까지)
    for (let i = 0; i < firstDay; i++) {
        const emptyDiv = document.createElement("div");
        calendarDays.appendChild(emptyDiv);
    }

    // 날짜 추가
    for (let date = 1; date <= lastDate; date++) {
        const dayDiv = document.createElement("div");
        dayDiv.className = "day";
        dayDiv.innerText = date;

        // 현재 날짜의 YYYY-MM-DD 포맷 생성
        const formattedDate = new Date(Date.UTC(currentYear, currentMonth, date)).toISOString().split('T')[0];

        // 오늘 날짜 강조 (새로고침할 때마다 오늘 날짜가 선택됨)
        if (selectedDate === formattedDate) {
            dayDiv.classList.add("selected");
        }

        // 날짜 클릭 이벤트
        dayDiv.onclick = () => {
            selectDate(formattedDate);
        };

        calendarDays.appendChild(dayDiv);
    }
}

// 날짜 선택 함수
function selectDate(formattedDate) {
    selectedDate = formattedDate; // 새로운 선택된 날짜 저장
	sessionStorage.setItem("selectedDate", selectedDate); // sessionStorage에 저장
    loadCalendar();  // 달력 다시 로드하여 선택한 날짜만 강조
    loadExerciseRecords();  // 운동 기록 다시 불러오기
}

// 이전 달로 이동 (선택된 날짜 초기화)
function goToPreviousMonth() {
    if (currentMonth === 0) {
        currentYear -= 1;
        currentMonth = 11;
    } else {
        currentMonth -= 1;
    }
    selectedDate = null; // 선택된 날짜 초기화
    loadCalendar();
}

// 다음 달로 이동 (선택된 날짜 초기화)
function goToNextMonth() {
    if (currentMonth === 11) {
        currentYear += 1;
        currentMonth = 0;
    } else {
        currentMonth += 1;
    }
    selectedDate = null; // 선택된 날짜 초기화
    loadCalendar();
}


// 운동 기록 불러오기
async function loadExerciseRecords() {
    const recordsDiv = document.getElementById("records-container");
    recordsDiv.innerHTML = '';

    if (!selectedDate) return; // 선택된 날짜가 없으면 실행 X

    const userId = sessionStorage.getItem("userId");
    if (!userId) {
        recordsDiv.innerHTML = '<p>운동 날짜 또는 사용자 정보가 없습니다.</p>';
        return;
    }

    try {
        const response = await fetch(`/api/exercise/records`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ exerciseDate: selectedDate, userId })
        });

        if (!response.ok) throw new Error("운동 기록을 불러오는 중 오류 발생");

        const records = await response.json();
        if (records.length === 0) {
            recordsDiv.innerHTML = '<p>운동 기록이 없습니다.</p>';
        } else {
            records.forEach(record => {
                const recordElement = document.createElement("div");
                recordElement.className = 'record-item';

                // 운동 시간을 시간과 분으로 변환
                const hours = Math.floor(record.climbTime / 60);
                const minutes = record.climbTime % 60;
                let timeString = "";
                if (hours > 0) timeString += `${hours}시간 `;
                timeString += `${minutes}분`;

                recordElement.innerHTML = `
                    <strong>운동 장소:</strong> ${record.climbPlace}<br>
                    <strong>난이도:</strong> ${record.climbStage}<br>
                    <strong>시도 횟수:</strong> ${record.climbCount}<br>
                    <strong>운동 시간:</strong> ${timeString}<br>
					<strong>소모 칼로리:</strong> ${record.climbKcal}kcal<br>
                `;
                recordsDiv.appendChild(recordElement);
            });
        }
    } catch (error) {
        recordsDiv.innerHTML = '<p>운동 기록을 불러오는 중 오류가 발생했습니다.</p>';
    }
}

// 운동 기록 추가 페이지 이동
document.getElementById("record-button").onclick = () => {
    const userId = sessionStorage.getItem("userId");

    if (!selectedDate || !userId) {
        alert("운동 날짜 또는 사용자 정보가 없습니다!");
        return;
    }

    alert(`운동 기록 페이지로 이동합니다: 날짜=${selectedDate}, 사용자 ID=${userId}`);
    window.location.href = '/exercise-add';
};

// 버튼 클릭 이벤트 추가
document.getElementById("prev-month-btn").onclick = goToPreviousMonth;
document.getElementById("next-month-btn").onclick = goToNextMonth;

// 페이지 로드 시 실행
window.onload = () => {
    selectedDate = null; // 페이지 로드 시 무조건 오늘 날짜만 선택되도록 설정
    loadCalendar();
    loadExerciseRecords();
};
