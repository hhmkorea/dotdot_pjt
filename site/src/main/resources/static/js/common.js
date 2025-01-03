$(document).ready(function(){
	// gnb
	$("nav > ul > li.has_sub > a").click(function(e){
		if($(this).parent().has("> ul")) {
			e.preventDefault();
		}

		if(!$(this).hasClass("on")) {
			$(this).next("ul").stop().slideDown(200);
			$(this).addClass("on");
			$(this).parent().siblings().find(" > a").removeClass("on").end().find(" > ul").stop().slideUp(200);
		}else if($(this).hasClass("on")) {
			$(this).removeClass("on");
			$(this).next("ul").stop().slideUp(200);
		}
	});

	// menu_toggle
	$(".menu_toggle").click(function(){
		$('#container .menu_toggle').toggleClass('active');
		$('body').toggleClass('snb_none');
		$(window).trigger('resize');
	});
	// cm_list
	$(".cm_list > div > a").click(function(){
		let submenu = $(this).next("div.hide_view");
		if( submenu.is(":visible") ){
			submenu.removeClass("open");
		}else{
			submenu.addClass("open");
		}
	});

	// 댓글
	$(".cm_re_info > button").click(function(){
		let submenu = $(this).parent().next("div.hide_view");
		if( submenu.is(":visible") ){
			submenu.removeClass("open");
		}else{
			submenu.addClass("open");
		}
	});

	// 첨부파일
	$(".file_input input[type='file']").on('change',function(){
		let fileName = $(this).val().split('/').pop().split('\\').pop();
		$(this).parent().siblings("input[type='text']").val(fileName);
	});
	// 파일업로드 미리보기
	$('.file_upload input[type=file]').change(function(event) {
		let tmppath = URL.createObjectURL(event.target.files[0]);
		$(this).parent('label').parent('.file_upload').parent('.file_preview').find("img").attr('src',tmppath);
	});

	// 섬머노트
	$('.summernote').summernote({
		codeviewFilter: false,                              // 코드 보기 필터 비활성화
		codeviewIframeFilter: false,                        // 코드 보기 iframe 필터 비활성화

		height: 300,                 // 에디터 높이
		minHeight: null,             // 최소 높이
		maxHeight: null,             // 최대 높이
		focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
		lang: "ko-KR",					// 한글 설정
		placeholder: '최대 2048자까지 쓸 수 있습니다',	//placeholder 설정
		disableResizeEditor: true,
		toolbar: [
			['fontname', ['fontname']],                     // 글꼴 설정
			['fontsize', ['fontsize']],                     // 글자 크기
			['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],  // 글자 스타일 설정
			['color', ['forecolor','color']],               // 글자색
			['table', ['table']],                           // 표 생성
			['insert', ['picture', 'link','video']],        // 이미지, 링크 , 동영상
			['para', ['ul', 'ol', 'paragraph']],            // 문단 스타일 설정
			['height', ['height']],                         // 줄간격
			['view', ['codeview','fullscreen', 'help']]     // 코드보기, 전체화면, 도움말
		],
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'], // 추가한 글꼴
		fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'], // 추가한 폰트사이즈
		callbacks : {
			// 파일 업로드
			onImageUpload : function (files) {
				for(let i=0; i < files.length; i++){
					// 이미지가 여러개일 경우
					imageUpload(files[i]);
				}
			},
			// 파일 삭제
			onMediaDelete: function ($target){
				if(confirm("이미지를 삭제하시겠습니까?")){
					let fileName = $target.attr('src').split('/').pop();
					deleteFile(fileName);
				}
			}
		}
	});

});

// 이미지 업로드
function imageUpload(file){
	let id = $("#id").val();
	let formData = new FormData();
	formData.append("file", file);

	$.ajax({
		url : "/upload/imageUpload/" + id,
		type : "POST",
		data : formData,
		// contentType, processData 설정 안하면 TypeError: Illegal invocation 오류가 발생한다
		contentType: false,
		processData: false,
		encType : "multipart/form-data",
		success : function (data) {
			// 글에 이미지 넣을때 크기 설정
			// $("#summernote").summernote("insertImage", "/uploadPath/image/"+data, function (data){
			//     data.css("width" , "100%");
			// });

			// 글에 이미지 넣기
			$("#content").summernote("insertImage", "/uploadPath/image/" + id +"/" + data);
		},
		error(e){
			console.log("error : "+ e);
		}
	});
}

// 이미지 삭제
function deleteFile(fileName) {
	let id = $("#id").text();
	let formData = new FormData();
	formData.append("file", fileName);

	$.ajax({
		url : "/upload/imageDelete/" + id,
		type : "POST",
		data : formData,
		// contentType, processData 설정 안하면 TypeError: Illegal invocation 오류가 발생한다
		contentType: false,
		processData: false,
		encType : "multipart/form-data"
	});
}

// 레이어 팝업(기본)
function layerPop(popName){
	let $layer = $("#"+ popName);
	$layer.fadeIn(500).css('display', 'inline-block').wrap( '<div class="overlay_t"></div>');
	$('body').css('overflow','hidden');
}
function layerPopClose(){
	$(".popLayer").hide().unwrap( '');
	$('body').css('overflow','auto');
	$(".popLayer video").each(function() { this.pause(); this.load(); });
}
function layerPopClose2(popName){
	$("#"+ popName).hide().unwrap( '');
	$('body').css('overflow','auto');
}

// 클릭시 새창 팝업 띄우기
function popup_win(str,id,w,h,scrollchk){
	let pop = window.open(str,id,"width="+w+",height="+h+",scrollbars="+scrollchk+",resize=no,location=no ");
	pop.focus();
}

// 소스 출처 : https://congsong.tistory.com/16