$(function(){
    // [추가] 버튼 누르면 될 첨부파일 추가 가능
    var i = 0;
    $("#btnAdd").click(function(){
       $("#files").append(`
               <div class="input-group mb-2">
                   <input class="form-control col-xs-3" type="file" name="upfile${i}"/>
                   <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
               </div>`);
       i++;
    });

    //Summernote 추가
    ${'#content'}.summernote({
        height: 300,
    });

});


function deleteFiles(fileId){
   // 삭제할 file 의 id 값(들)을 #delFiles 에 담아 submit 한다
   $("#delFiles").append(`<input type='hidden' name='delfile' value='${fileId}'>`);
}
