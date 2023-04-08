$(function(){
   // 글 [삭제] 버튼
   $("#btnDel").click(function(){
        let answer = confirm("삭제하시겠습니까?");
        if(answer){
            $("form[name='frmDelete']").submit();
        }
   });

   //현재 글의 id값
   const id = $("input[name='id']").val().trim();

   // 현재 글의 댓글을 불러온다.
   loadComment(id);

    // 댓글 작성 버튼 누르면 댓글 등록 하기.
    // 1. 어느글에 대한 댓글인지? --> 위에 id 변수에 담겨있다
    // 2. 어느 사용자가 작성한 댓글인지? --> logged_uid 값
    // 3. 댓글 내용은 무엇인지?  --> 아래 content
    $("#btn_comment").click(function(){
        console.log("### log comment 버튼 클릭");

        //입력한 댓글
        const content = $("#input_comment").val().trim()

        //검증
        if(!content){
            alert("댓글을 입력하세요");
            $(input_comment).focus();
            return;
        }

        //검증 통과시, 전달할 paramater 들 준비
        const data = {
            "board_id" : id,
            "user_id" : logged_id,
            "content" : content
        };

        $.ajax({
            url : conPath+ "/comment/write",
            type: "POST",
            data : data,
            cache : false,
            success : function(data, status, xhr){
                if(status == "success"){
                    if(data.status !== "OK"){
                        alert(data.status);
                        return;
                    }
                    loadComment(id);        //댓글 목록 다시 업데이트
                    $("#input_comment").val('');        //입력란 리셋
                }
            },
        });
    });


});


// 글(board_id)의 댓글 목록 읽어오기
function loadComment(board_id){
    $.ajax({
        url: conPath + "/comment/list?id=" + board_id,
        type: "GET",
        cache: false,
        success: function(data,status){
            if(status == "success"){
                //서버쪽 에러 메세지 있는 경우
                if(data.status !== "OK"){
                    alert(data.status);
                }

                buildComment(data);     //화면 렌더링
            }
        },
    });


    addDelete();
}

function buildComment(result){
    $("#cmt_cnt").text(result.count);       //댓글 총 개수

    const out = [];

    result.data.forEach(comment => {
        let id = comment.id;
        let content = comment.content.trim();
        let regdate = comment.regdate;

        let user_id = parseInt(comment.user.id);
        let username = comment.user.username;
        let name = comment.user.name;

        //삭제 버튼 여부: 로그인 한 사용자가 작성자인 경우에만 삭제 버튼이 보이게 해야 한다.
        const delBtn = (logged_id !== user_id) ? '' : `<i class="btn fa-solid fa-delete-left text-danger" data-bs-toggle="tooltip"
                                                              data-cmtdel-id="${id}" title="삭제"></i>
                                                        `;

        const row = `
            <li class="comment">
                <div class="vcard bio">
                    <img src="../images/profile.png" alt="Image placeholder">
                </div>
                <div class="comment-body">
                    <h3>${username}</h3>
                    <div class="meta">${regdate}</div>
                    <p>${content}</p>
                </div>
            </li>
        `;

        out.push(row);
    });

    $("#cmt_list").html(out.join("\n"));
}// end buildComment();


// 댓글 삭제버튼이 눌렸을때. 해당 댓글 삭제하는 이벤트를 삭제버튼에 등록
function addDelete(){

    //현재 글의 id
    const id = $("input[name='id']").val().trim()


    $("[data-cmtdel-id]").click(function(){
       if(!confirm("댓글을 삭제하시겠습니까?")) return;

       //삭제할 댓글의 comment_id
       const comment_id = $(this).attr("data-cmtdel-id");

       $.ajax({
            url : conPath + "/comment/delete",
            type : "POST",
            cache: false,
            data : {"id" : comment_id},
            success : function(data, status, xhr){
                if(status == "status"){
                    alert(data.status);
                    return;
                }
                loadComment(id);
            }
       });
    });
}

