$(document).ready(function(){

    $("#logout").click(function(e){
        e.preventDefault();
        $("#logout-form").submit();
    });



// До лучших времен
    $("#search-form").submit(function (event) {
        event.preventDefault();
        var query = $("#input-search").val();
        location.href = "/search?search=" + query
    })

});