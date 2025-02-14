/**
 * 대시보드~~
 */
google.charts.load("current", {packages:["calendar"]});
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

// ============================= 차트 전체 호출
function drawChart() {
	//const thisDateData = loadCurrentMonth(0);
    
	// Google Charts 라이브러리가 로드되면 실행
    loadCalendarGraph();	// 캘린더 그래프
    loadDifficultyChart();	// 누적 난이도 그래프 로드
	loadTotalTimeData();	// 이번달 총 운동량
	loadHighstScoreData();	// 이번달 최고 운동 내용
	
	aboutLoadDifficulty();	// 누적난이도 안내 버튼
}

// ============================= 기준 달 설정
function loadCurrentMonth(offset){
	const now = new Date();
	now.setDate(1);
	
	// offset값 적용
		// + 1 -> 1달 후
		// - 1 -> 1달 전
	now.setMonth(now.getMonth() + offset);
	//console.log("기준 날짜 -> " + now.getMonth());
	
	
	const year = now.getFullYear();
	const month = String(now.getMonth() + 1).padStart(2, '0'); // 2자리로 맞추기

	const formattedDate = `${year}-${month}`;
	
	
	return formattedDate;
}

// ============================= 캘린더 그래프
function loadCalendarGraph() {
	const startDate = "2025-01-01";
	const endDate = "2025-12-31";
		
    const query = `/api/getCalendarData?startDate=${startDate}&endDate=${endDate}`;
    return fetch(query)
        .then(response => response.json())
        .then(data => {
           // console.log("달력 데이터(Fetch후): ", data);

            // 구글 캘린더 열 추가
            const dataTable = new google.visualization.DataTable();
            dataTable.addColumn({ type: 'date', id: 'Date' });
            dataTable.addColumn({ type: 'number', id: 'Won/Loss' });

            const rows = data.map(item => {
                // 멀쩡하니
                if (!item.exerciseDate || item.checkPoint === undefined) {
                    console.error("Invalid item: ", item);
                    return [null, null];  // 잘못된 데이터는 [null, null]로 리턴
                }

                // exerciseDate -> "YYYY-MM-DD" 분리 -> Date 객체변환
                const dateParts = item.exerciseDate.split("-");
                const date = new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);

                // 데이터 행반환
                return [date, item.checkPoint];
            });

            // 데이터 테이블에 행 추가
            dataTable.addRows(rows.filter(item => item[0] !== null && item[1] !== null));

            // 캘린더 차트 객체
            var chart = new google.visualization.Calendar(document.getElementById('commitHeatmap'));

            var options = {
                title: "출석체크",
                height: 500,
				
				//데이터 True칸
                colorAxis: {
                    minValue: 0,
                    colors: ['#FFFFFF', '#E5C3FF'] // 점점 연보라색 그라데이션
                },
				//데이터 False칸
                noDataPattern: {
                    backgroundColor: '#FFFFFF'
                }
            };

            // 차트 그리렴
            chart.draw(dataTable, options);
        })
        .catch(error => {
            console.error("달력에러: ", error);
        });
}

// ============================= 누적 난이도 그래프 로드 함수
function loadDifficultyChart() {
    const currentMonth = loadCurrentMonth(0);    
    const startDate = loadCurrentMonth(-2);
    const endDate = currentMonth;
    
    var chart = new google.visualization.BarChart(document.getElementById('difficultyChart'));        

    // DB 쿼리문 -> 데이터 호출
    const query = `/api/getComboChartData?startDate=${(startDate + "-01")}&endDate=${(endDate + "-31")}`;
    fetch(query)
        .then(response => response.json())
        .then(data => {
            //console.log("콤보 데이터:", data);

            // 총 10다ㅏㄴ계
            const stages = ['1단계', '2단계', '3단계', '4단계', '5단계', '6단계', '7단계', '8단계', '9단계', '10단계'];

            // 월별 데이터 - 정렬(오름차순)
            let chartData = Object.entries(data)
                .sort(([monthA], [monthB]) => monthA.localeCompare(monthB))
                .map(([month, stageData]) => {
                    let row = [month];

                    let totalStages = 0;
                    let totalCount = 0;

                    // 각 단계에 대해 값을 넣고, 없으면 0으로 채움 -> 없으면 단계 땡겨짐
                    stages.forEach((stage, index) => {
                        const count = stageData[index + 1] || 0; // index+1로 접근 -> 밀림
                        row.push(count);

						// 레벨 가중치 부과 -> 평균 구하기
						if(count != 0) {
					        totalStages += (index + 1) * count;
					        totalCount += count;
					    }
                        
                    });

                    // 평균 계산 (0으로 나누는 경우 방지)
                    const avg = totalCount > 0 ? totalStages / totalCount : 0;

                    // 평균 값을 추가
                    row.push(avg);
                    return row;
                });

            // 데이터
            var dataTable = google.visualization.arrayToDataTable([
                    ['Month', '1단계', '2단계', '3단계', '4단계', '5단계', '6단계', '7단계', '8단계', '9단계', '10단계', '평균레벨'],
                    ...chartData
                ]);
                
            var options = {
                seriesType: 'bars',
				//colors: ['7730AE','E5C3FF','FFCD4A','7E688E'],
				series: {10: {
					type: 'line',
					color: '7730AE',
					lineWidth: 1.5,
					pointShape: 'round'
				}},
				isStacked: true
            };

			// 그려
            chart.draw(dataTable, options);
            
        })
        .catch(error => console.error("누적난이도에러:", error));
}

// ============================= 누적 난이도 설명 표
function aboutLoadDifficulty(){
	//console.log("123");
	const showAbout = document.getElementById("aboutDifficulty");
	
	//console.log(showAbout);
	
	// 마우스 올리면
	showAbout.addEventListener('mouseover',function(e){
		//console.log("123"+e);
		
	});
	
	// 마우스 나가면
	showAbout.addEventListener('mouseout',function(e){
		//console.log("456"+e);
		
	});
	
}

function TestFunction(){
	//console.log("789");
}

	
// ============================= 이번달 총 운동량
function loadTotalTimeData() {
	
	const currentMonth = loadCurrentMonth(0);
	const startDate = currentMonth + "-01";
	const endDate = currentMonth + "-31";
	console.log("currentMonth " + currentMonth);
	
	const DataDiv = document.getElementById("monthlyDataSection");
	DataDiv.innerHTML = "<h2>" + currentMonth.split('-')[1] + "월 기록</h2>";
	
	let str = "<h4>";
	
	// DB 쿼리문 -> 데이터 호출
	const query = `/api/getTotalTimeData?startDate=${startDate}&endDate=${endDate}`;
	return fetch(query)
	    .then(response => response.json())
	    .then(data =>{
		        //console.log("월별 시간 데이터(Fetch후): ", data);

				// 데이터 처리
		        Object.entries(data).forEach(([date, value]) => {
		            //console.log(`Date: ${date} | Total Time: ${value}`);
		            
					// 데이터 필터
		            if (date === currentMonth) {
						const totalClimbCount = value.ClimbCount || 0;
						const totalClimbTime = value.ClimbTime || 0;
						str += "총 " +  totalClimbCount + "회</h4><h4>총 " + totalClimbTime + "시간</h4>";
						DataDiv.innerHTML += str;
		            }
		        });
				
		    })
		    .catch(error => console.error("왼쪽구석에러:", error));
}
	

// ============================= 이번달 최고점수
function loadHighstScoreData() {
	
	const currentMonth = loadCurrentMonth(0);
	const startDate = currentMonth + "-01";
	const endDate = currentMonth + "-31";
	console.log("currentMonth " + currentMonth);
	
	const DataDiv = document.getElementById("monthlyHighstScoreData");
	
	let str = "<h3>이번달 최고 점수(1회당)</h3>";
	
	// DB 쿼리문 -> 데이터 호출
	const query = `/api/getHighstScoreData?startDate=${startDate}&endDate=${endDate}`;
	return fetch(query)
	    .then(response => response.json())
	    .then(data =>{
				//console.log("123 -> " + str2)
				
				str += "<h4>최고난이도: " + data.stage + " 레벨</h4>";
				str += "<h4>최고시간: " + data.time + " 시간</h4>";
				
				DataDiv.innerHTML = str;
			})
		    .catch(error => console.error("최고점수에러:", error));
}