$(document).ready(function () {

    var token = $("#token").val();

    $("#main").on("click", "button", function (event) {
        console.log(this.id);
        var button =$(this);
        relation(this.id, token, button)
    });

// <div class="card bg-info mb-3" style="max-width: 100%;">
//         <div class="card-header"><p
//     th:text="${user.name +' '+user.subname}"></p></div>
//     <div class="card-body">
//         <p> Some text</p>
//     </div>
//     </div>
    function relation(id, token, button) {
        var formData = {
            id: id
        };

        $.ajax({
            headers: {
                "X-CSRF-TOKEN": token,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "post",
            data: JSON.stringify(formData),
            url: "/relation",
            async: false,
            dataType: "json",
            success: function (response) {
                if (button.hasClass("btn-outline-success")){
                    button.removeClass("btn-outline-success");
                    button.addClass("btn-outline-info");
                    button.html('Follow');
                }
                else{
                    button.removeClass("btn-outline-info");
                    button.addClass("btn-outline-success");
                    button.html('Unfollow');
                }
            }
        });

    }
});
