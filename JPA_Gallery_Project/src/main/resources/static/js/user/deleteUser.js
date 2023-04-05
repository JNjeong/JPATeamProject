$(function(){
    $("button[data-id]").click(function(){
        alert("회원 탈퇴 되었습니다.");
        $("[name='id']").val($(this).attr("data-id"));
        $("[name='formDelete']").submit();
    });
});
