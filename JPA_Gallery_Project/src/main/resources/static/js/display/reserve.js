$(document).ready(function(){
    $('#pick').prop('disabled', true);

    $('.datepicker')
        .datepicker({
            format: 'yyyy-mm-dd',
            startDate: '/*[[${display.dp_start}]]*/',
            endDate: '/*[[${display.dp_end}]]*/',
            todayHighlight: true,
            toggleActive: true,
            autoclose: true,
            language: 'ko'

        })
        .on('changeDate', function(){
            //alert($('#dp1').val());  //

            $('#pickdate').empty();
            $('#pickdate').append($('#dp1').val());
           // action='modify';
           // type = 'PUT';
            let data1 = $('#dp1').val();
            let data2 = /*[[${display.dp_seq}]]*/ "dp_seq";

           // alert(data2);  //

            var results = $.ajax({
                url: "/display/getCount",
                method: "POST",
               data:{data1, data2},
                dataType: "json",
                success: function(data){
                    //alert(data);
                    $("#seatCount").empty();
                    $("#seatCount").text(data);
                    if(data == 0){
                        $('#pick').prop('disabled', true);
                    } else {
                       $('#pick').removeAttr('disabled');
                       $('[name=visitDate]').val($('#dp1').val());
                    }
                },
            });

        });

       // results.done(function(data){


        // });

});


