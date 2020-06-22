$(document).ready(function () {

    var token = $("#token").val();
    var id = getUrlParameter('id');
    console.log();

    $("#form-post-submit").submit(function (event) {
        event.preventDefault();
        var record = $("#post").val();
        console.log(token);
        addPost(record, token);

    });

// <div class="card bg-info mb-3" style="max-width: 100%;">
//         <div class="card-header"><p
//     th:text="${user.name +' '+user.subname}"></p></div>
//     <div class="card-body">
//         <p> Some text</p>
//     </div>
//     </div>
    function getPosts() {
        $.getJSON("getpost", function (data) {
            console.log(data)
            // let tbl = $("#tablebody");
            // $.each(data,function (i,item) {
            //     let tr = $("<tr>");
            //     let td = $("<td>");
            //     td.append($("<a>",{
            //         'href':"book?bookId="+item.id,
            //         'text':item.title
            //     }));
            //     tr.append(td);
            //     tr.append($("<td>",{'text':item.authors}));
            //     tr.append($("<td>",{'text':item.year}));
            //     tr.append($("<td>",{'text':item.genre}));
            //     tbl.append(tr);
            // })
        })

    }

    function getUrlParameter(sParam) {
        var sPageURL = window.location.search.substring(1),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
            }
        }
    };

    function addPost(record, token) {
        var formData = {
            record: record,
            likes: 0,
            dislikes: 0
        };

        $.ajax({
            headers: {
                "X-CSRF-TOKEN": token,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "post",
            data: JSON.stringify(formData),
            url: window.location + "addpost",
            async: false,
            dataType: "json",
            success: function (response) {

            }
        });


    }


});
