$(document).ready(function(){
    $("[data-img]").click(function(){
        //alert($(this).attr('data-img'));
        var frm = $(this).parent();
        frm.attr("method", "POST")
        frm.attr("action", "edetail")
        frm.submit();
    });
});