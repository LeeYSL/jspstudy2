<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 사진 등록</title>
<link rel="stylesheet" href="../../css/main.css">
</head>
<body>
<h3>사진업로드</h3>
<table>
<tr><td><img id="preview" src=""></td></tr>
<tr><td>
<form action="picture" method="post"
      enctype="multipart/form-data">
    <input type="file" name="picture" id="imageFile" accept="img/*">
    <input type="submit" value="사진등록">
</form></td></tr></table>
<script type="text/javascript">
//#imageFile :  <input type="file"...> 태그 객체 (참조중)
let imagefile = document.querySelector('#imageFile'); 
//#preview : <img id="preview" src=""> 태그 객체
let preview = document.querySelector('#preview');
//imagefile 새로운 파일 선택시 발생되는 이벤트 등록 function 실행해줘?
imagefile.addEventListener('change',function(e) { //이벤트핸들러
	let get_file = e.target.files; //선택된 파일
	let reader = new FileReader(); //파일을 읽기 위한 스트림
	reader.onload = (function(Img) { //Img<=preview
		return function(e) {
			Img.src =e.target.result; //선택 된 파일의 이름 
			
		}
		
	})(preview);
	//get_file :선택 된 파일이 존재한다면 
	//get_file[0] :선택 된 파일 중에 첫번째 파일
	// reader.readAsDataURL : 파일 읽기 = > onload 이벤트 발생
	if(get_file){  reader.readAsDataURL(get_file[0]); } 
});

</script>
</body>
</html>