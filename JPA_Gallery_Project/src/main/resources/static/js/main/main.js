   $(document).ready(function(){
        $.ajax({
            url: 'http://openapi.seoul.go.kr:8088/774e64616372626638354f6576584d/xml/ListExhibitionOfSeoulMOAInfo/1/100/',
            type: 'GET',
            dataType: 'xml',
            success: function(response){
                xmlParsing1(response);
            },
            error: function(xhr, status, msg){
                console.log('상태값 : ' + status + 'http 에러 메세지 : ' + msg)
            },
        });

       function xmlParsing1(xmlDOM){
            var i;
            var html = '';
            var row = xmlDOM.getElementsByTagName("row");
            for(i=0; i<3; i++){
                var r = Math.floor(Math.random() * 100);
				var title = row[r].getElementsByTagName("DP_NAME")[0].childNodes[0].nodeValue;
				var img = row[r].getElementsByTagName("DP_MAIN_IMG")[0].childNodes[0].nodeValue;

                html += '<div class="col-lg-4 text-center">' +
                            '<div>' + '<div data-jarallax-element="-100" class="jarallax">';
                html += '<h5 class="d-inline-block title-museum">' + title + '</h5>';
                html += '<img src="' + img + '" alt="Image" class="img-fluid my-3 my-lg-5 img-museum">'
                html += '</div></div></div>'
            }
       document.getElementById("museumDiv").innerHTML = html;
       }
   });

   $(document).ready(function(){
        $.ajax({
            url: 'http://openapi.seoul.go.kr:8088/6b416647737262663530774c507371/xml/SemaPsgudInfoKorInfo/1/100/',
            type: 'GET',
            dataType: 'xml',
            success: function(response){
                xmlParsing2(response);
            },
            error: function(xhr, status, msg){
                console.log('상태값 : ' + status + 'http 에러 메세지 : ' + msg)
            },
        });

       function xmlParsing2(xmlDOM){
            var i;
            var html = '';
            var row = xmlDOM.getElementsByTagName("row");
            for(i=0; i<4; i++){
                var r = Math.floor(Math.random() * 100);
				var title = row[r].getElementsByTagName("prdct_nm_korean")[0].childNodes[0].nodeValue;
				var img = row[r].getElementsByTagName("thumb_image")[0].childNodes[0].nodeValue;
				var author = row[r].getElementsByTagName("writr_nm")[0].childNodes[0].nodeValue;

                html += '<div class="col-lg-3 text-center">' +
                            '<div>' + '<div data-jarallax-element="-100" class="jarallax">';
                html += '<h5 class="d-line-block col-lg-12 title-museum"><b>' + title + '</b><br/>' + author + '</h5>';
                html += '<img src="' + img + '" alt="Image" class="img-fluid my-3 my-lg-5 img-museum">'
                html += '</div></div></div>'
            }
                document.getElementById("exhibition").innerHTML = html;

       }
   });