$(document).ready(function () {
    var token = $("#token").val();
    var page = 0;
    $("#next-page").click(function (e) {
        page=page+1;
        getAllPosts(page);
    });
    getAllPosts(page);
    $("#form-post-submit").submit(function (event) {
        event.preventDefault();
        var record = $("#post").val();
        $("#post").val('');
        $("#main").find("div").remove();
        addPost(record, token);

    });
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
                page=0;
                getAllPosts(page);
            }
        });


    }

    function getAllPosts(page) {


        var formData = {
            id: page
        };

        $.ajax({
            headers: {
                "X-CSRF-TOKEN": token,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "post",
            data: JSON.stringify(formData),
            url: window.location + "getposts",
            async: false,
            dataType: "json",
            success: function (response) {
                $.each(response, function (i,item) {
                    getUser(item)
                })
            }
        });

    }

    function getUser(item) {
        var formData = {
            id:item.id
        };
        $.ajax({
            headers: {
                "X-CSRF-TOKEN": token,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "post",
            data: JSON.stringify(formData),
            url: window.location + "getuser",
            async: false,
            dataType: "json",
            success: function (response) {
                console.log(response);
                var record = $("#main");
                var divCard = $("<div class=\"card bg-light mb-3\" style=\"max-width: 100%;\">");
                var divHead = $("<div class=\"card-header\">");
                var pName = $("<a>", {
                    'href': window.location + "user?userId=" + response.id,
                    'text': response.name + ' ' +response.subname
                });
                var divBody = $("<div class=\"card-body\">");
                var pRecord = $("<p>").text(item.record);
                divBody.append(pRecord);
                divHead.append(pName);
                divCard.append(divHead);
                divCard.append(divBody);
                record.append(divCard);
            }
        });


    }
});
