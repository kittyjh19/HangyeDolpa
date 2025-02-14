/**
 * 지도부분~~~
 */

// 기본 검색 위치 -> 역삼역
	var mapCenter = new kakao.maps.LatLng(37.500725285, 127.036600396);
	
	var markers = [];
	
	var mapContainer = document.getElementById('map') // 지도를 표시할 div 
	
	var defaultOption = {
	        center: mapCenter, // 지도의 중심좌표
	        level: 3, // 지도의 확대 기본값: 3
	        radius: 1000
	    };
	
	// 플래그 선언 -> 지도 사이즈 관련 플래그
	let flag = false;
 	const clickedPlaceDiv = document.getElementById("clickedPlace");
	const mapDiv = document.querySelector(".map_wrap");
	
	resizeMapDiv();
	
	// 처음 지도 생성
	var map = new kakao.maps.Map(mapContainer, defaultOption); 
	
	// 마커 관련
	// var centerMarker = new kakao.maps.Marker({
	//    map: map,
	//    position: new kakao.maps.LatLng(map.getCenter())
	//});
	//markers.push(centerMarker);
	
	// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
	var infowindow = new kakao.maps.InfoWindow({zIndex:1});
	
	// 키워드로 장소를 검색합니다
	searchPlaces();
	
	// 검색 함수
	function searchPlaces() {
		// 장소 검색 객체
		var ps = new kakao.maps.services.Places(map);
		
		// 지도 범위 재설정
		//map.relayout();
		
		// 장소 검색 옵션(추가중)
		let NLevel = map.getLevel();
		
		const options = {
				location: map.getCenter(), 
				sort: kakao.maps.services.SortBy.DISTANCE,
				level: NLevel,
				size: 10
		}
		
		// console.log("Place Function Called! centerValue: " + map.getCenter() + "| reLevel: " + map.getLevel());
	    
		// 검색객체 -> 콜백함수 -> Data 처리
	    ps.keywordSearch("클라이밍", placesSearchCB, options); 
	}
	
	// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
	function placesSearchCB(data, status, pagination) {
	    if (status === kakao.maps.services.Status.OK) {

	    	// 정상적으로 검색 완료 -> 마커 파바박
	        displayPlaces(data);
	
	    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
	
	        alert('검색 결과가 존재하지 않습니다.');
	        return;
	
	    } else if (status === kakao.maps.services.Status.ERROR) {
	
	        alert('검색 결과 중 오류가 발생했습니다.');
	        return;
	
	    }
	}
	
	// 검색 결과 목록과 마커를 표출하는 함수입니다
	function displayPlaces(places) {
	
	    var listEl = document.getElementById('placesList'), 
	    menuEl = document.getElementById('menu_wrap'),
	    fragment = document.createDocumentFragment(), 
	    bounds = new kakao.maps.LatLngBounds(), 
	    listStr = '';
	    
	    // 검색 결과 목록에 추가된 항목들을 제거합니다
	    removeAllChildNods(listEl);
	
	    // 지도에 표시되고 있는 마커를 제거합니다
	    removeMarker();
	    
	    var marke0Center = map.getCenter();
	    
	    for (var i=0; i<places.length; i++ ) {
	
	        // 마커를 생성하고 지도에 표시합니다
	        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
	            marker = addMarker(placePosition, i), 
	            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다
			
	        // 중심 마커 설정 및 
            if(i == 0){
            	//중심 latlng 객체
	        	marke0Center = marker.getPosition();
	        }

	        // 마커 바운드
	        bounds.extend(placePosition);
	        
	        // 마커와 검색결과 항목에 mouseover 했을때
	        // 해당 장소에 인포윈도우에 장소명을 표시합니다
	        // mouseout 했을 때는 인포윈도우를 닫습니다
	        (function(marker, title) {
	        	
	        	let val = 0;
	        	
	            kakao.maps.event.addListener(marker, 'mouseover', function() {
	                displayInfowindow(marker, title);
	                //console.log("distance: " + distance);
	            });
	            
	            // KEC) 마커 누르면 함수
	            kakao.maps.event.addListener(marker, 'click', function(){
	            	getPlaceUrl(title, placePosition.getLng(), placePosition.getLat());
	            });
	
	            kakao.maps.event.addListener(marker, 'mouseout', function() {
	                infowindow.close();
	            });
	
	            // 마커 기준 지도 중심 이동 -> 애니메이션시 onover함수 써야함? 한 칸 단위로 이동하려면?
	            itemEl.onmouseover =  function () {
	            	
	                displayInfowindow(marker, title);
	                var markerLatLng = marker.getPosition();
	                
	                map.setCenter(markerLatLng);
	            };
	
	            itemEl.onmouseout =  function () {
	                infowindow.close();
	            };
	            
	            // KEC) 마커 누르면 함수
	            itemEl.onclick = function(){
	            	getPlaceUrl(title, placePosition.getLng(), placePosition.getLat());

	            }
	            
	        })(marker, places[i].place_name);
	
	        fragment.appendChild(itemEl);
	    }
	
	    // 검색결과 항목들을 검색결과 목록에 추가
	    listEl.appendChild(fragment);
	    menuEl.scrollTop = 0;
	
	    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다 
	    // -> 지도 범위 재설정 과정에서 레벨링 및 좌표 변경됨을 확인
	    // 마커들이 넘 많아서 지도 레벨이 높아지는 거임!!!
	    map.setBounds(bounds);
	   
	    // 이후 강제적으로 센터를 맞춤 - 마커의 1번 녀석
	    map.setCenter(marke0Center);
	    //map.setLevel(5);
	}
	
	// 검색결과 항목을 Element로 반환하는 함수입니다
	function getListItem(index, places) {
	
	    var el = document.createElement('li'),
	    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
	                '<div class="info">' +
	                '   <h5>' + places.place_name + '</h5>';
	
	    if (places.road_address_name) {
	        itemStr += '    <span>' + places.road_address_name + '</span>' +
	                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
	    } else {
	        itemStr += '    <span>' +  places.address_name  + '</span>'; 
	    }
	                 
	      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
	                '</div>';           
	
	    el.innerHTML = itemStr;
	    el.className = 'item';
	
	    return el;
	}
	
	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
	function addMarker(position, idx, title) {
	    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
	        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
	        imgOptions =  {
	            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
	            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
	            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
	        },
	        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
	            marker = new kakao.maps.Marker({
	            position: position, // 마커의 위치
	            image: markerImage 
	        });
	
	    marker.setMap(map); // 지도 위에 마커를 표출합니다
	    markers.push(marker);  // 배열에 생성된 마커를 추가합니다
	
	    return marker;
	}
	
	// 지도 위에 표시되고 있는 마커를 모두 제거합니다
	function removeMarker() {
	    for ( var i = 0; i < markers.length; i++ ) {
	        markers[i].setMap(null);
	    }   
	    markers = [];
	}
	
	// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
	function displayPagination(pagination) {
	    var paginationEl = document.getElementById('pagination'),
	        fragment = document.createDocumentFragment(),
	        i; 
	
	    // 기존에 추가된 페이지번호를 삭제합니다
	    while (paginationEl.hasChildNodes()) {
	        paginationEl.removeChild (paginationEl.lastChild);
	    }
	
	    for (i=1; i<=pagination.last; i++) {
	        var el = document.createElement('a');
	        el.href = "#";
	        el.innerHTML = i;
	
	        if (i===pagination.current) {
	            el.className = 'on';
	        } else {
	            el.onclick = (function(i) {
	                return function() {
	                    pagination.gotoPage(i);
	                }
	            })(i);
	        }
	
	        fragment.appendChild(el);
	    }
	    paginationEl.appendChild(fragment);
	}
	
	// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
	// 인포윈도우에 장소명을 표시합니다
	function displayInfowindow(marker, title) {
	    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
	
	    infowindow.setContent(content);
	    infowindow.open(map, marker);
	}
	
	 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
	function removeAllChildNods(el) {   
	    while (el.hasChildNodes()) {
	        el.removeChild (el.lastChild);
	    }
	}
	 
	 // 마커나 아이템 누르면 URL 데이터
	 function getPlaceUrl(title, Xposition, Yposition){
		 //console.log("Fetch 데이터 전송 시작");
		 
		 let str = "/clickMarker?placeName=" + title + "&Xposition="+Xposition+"&Yposition="+Yposition;
		 
		 clickedPlaceDiv.innerHTML = "Data Loading...";
		 
		 //console.log("FetchURL -> "+str);
		 
         fetch(str)
         .then(resp => resp.text())
         //.then(data => console.log("Function Called URL-> " + data));
	 	 .then(data => {
	 		
	 		// TODO - 개느려.. 어떤 과정으로 개느려지는가
	        var iframeStr = '<iframe style="width: 100%; height: 100%; transform: scale(0.99); transform-origin: top left;" name="iframe" src="' + data + '"></iframe>';
	        //console.log(iframeStr);
	        
	        clickedPlaceDiv.innerHTML = iframeStr;
	        
	        makeHalfMap();
	    })
	    .catch(error => console.error('Error:', error));
	 }
	 
	 // 사이즈 변경 함수
	 function resizeMapDiv(){
		console.log("지도 부분 크기 변경 -> " + flag);
		
	    if (flag) {
	        // 지도랑 옆에꺼 50씩
	        mapDiv.style.flex = "1";
	        clickedPlaceDiv.style.flex = "1";
	        
	        // 버튼 활성화
	        document.getElementById("reSizingBtn").style.visibility = "visible";
	        
	    } else {
	        // 지도만 100
	        mapDiv.style.flex = "1";
	        clickedPlaceDiv.style.flex = "0";
	        console.log("Flag is False!!");
	        
	        //버튼 가리기
	        document.getElementById("reSizingBtn").style.visibility = "hidden";
	    }
	 }
	 
	 // 큰 지도 버튼 누르면 다시 커지기
	 function makeFullMap(){
		 console.log("왕지도 버튼 ON");
		 flag = false;
		 resizeMapDiv();
		 map.relayout();
	 }
	 
	 function makeHalfMap(){
		 console.log("작은지도 ON");
		 flag = true;
		 resizeMapDiv();
		 map.relayout();
	 }
	 
	 // 지도 중심 좌표 옮기고 재검색
	 function reFreshPlace(){
		console.log("중심좌표 이동 재검색");
		//var reCenter = map.getCenter();
		//var relevel = map.getLevel();
		//console.log("Re Fresh Called!" + reCenter + "Level: " + relevel);
		
		// 검색 객체에 지도 객체를 넣고 재검색으로 코드 변경
		searchPlaces();
	 }
	 
	 // 주소 검색 후 재검색
	 function reSearchAddPlace(){
		console.log("키워드 검색 시작");
		var searchKeyWord = '역삼';
		
		// 주소 검색 - 새창(작은거)
		new daum.Postcode({
		        oncomplete: function(data) {
		        	console.log("Postcode 시작")
		            //console.log(data.address);
		        	searchKeyWord = data.address;
		        	searchByKeyword(searchKeyWord);
		        }
		    }).open();
		
		// 새 창 -> String -> 검색 결과 선택
		console.log(searchKeyWord);
	 }
	 
	 // 선택한 결과 -> 주소 -> 좌표
	 function searchByKeyword(searchKeyWord){
		console.log("주소를 좌표로 검색 시작");
		// 주소를 좌표 객체
		var geocoder = new kakao.maps.services.Geocoder();
		
		// 콜백 함수
		var callback = function(result, status) {
		    if (status === kakao.maps.services.Status.OK) {
		    	console.log("정상 검색 완료");
		    	//console.log("Y, X => " + result[0].y + ", " + result[0].x);
		    	
		    	// 좌표로 지도 센터 이동	
		        map.setCenter(new kakao.maps.LatLng(result[0].y,result[0].x));
		 	    reFreshPlace();
	    	}
		    else if(status === kakao.maps.services.Status.ERROR){
		    	alert("에러 발생");
		    	return;
		    }
		    else if(status === kakao.maps.services.Status.ZERO_RESULT){
		    	alert("검색 결과가 없습니다");
		    	return;
		    }
		};
		
	// 검색
	geocoder.addressSearch(searchKeyWord, callback);
 }