// 이메일 인증번호
$(function(){
$("#checkEmail").click(function() {
   $.ajax({
      type : "POST",
      url : "login/mailConfirm",
      data : {
         "email" : $("#memail").val(),
      },
      success : function(data){
         alert("해당 이메일로 인증번호 발송이 완료되었습니다.")
         //console.log("data : " + data);
         chkEmailConfirm(data, memailconfirm, memailconfirmTxt);
      }
   })
})
});

