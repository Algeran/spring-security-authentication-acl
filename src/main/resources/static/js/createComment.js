$(document).ready(function () {
    $.ajax({
        url: "/getCommentData",
        type: "GET",
        success: function (commentDataDTO) {
            console.log(commentDataDTO);
            var completeHtml = "<h1>Comment Info:</h1>" +
                "" +
                "    <div class='row'>" +
                "        <label for='username-input'>Username: </label>" +
                "        <input class='username-input' id='username-input' type='text'/>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <label for='comment-input'>Comment: </label>" +
                "        <input class='comment-input' id='comment-input' type='text'/>" +
                "    </div>" +
                "    <div class='box'>" +
                "        <label for='books-input'>Authors: </label>" +
                "        <select class='books-input' id='books-input' size='5' multiple='multiple'>";
            var allBooks = commentDataDTO.allBooks;
            for (var i = 0, len = allBooks.length; i < len; ++i) {
                completeHtml += "<option value='" + allBooks[i].id+ "'>" + allBooks[i].name + "</option>";
            }
            completeHtml +=
                "        </select>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <button type='submit' name='action' value='save'>Save</button>" +
                "    </div>";

            $(".create-comment-form")
                .append(completeHtml);
        }
    });

    $(".create-comment-form").submit(function (event) {
        event.preventDefault();

        var commentCreateDTO = {
            id: null,
            username: $(this).find('input.username-input').val(),
            comment: $(this).find('input.comment-input').val(),
            books: $(this).find('select.books-input').val()
        };

        console.log("commentDTO[ id: " + commentCreateDTO.id +
            ", username: " + commentCreateDTO.username +
            ", comment: " + commentCreateDTO.comment +
            ", books: " + commentCreateDTO.books +
            "]");

        $.ajax({
            url: "/createComment",
            contentType : "application/json",
            data: JSON.stringify(commentCreateDTO),
            dataType : 'text',
            type: "POST",
            success: function (data) {
                console.log(data);
                alert('comment created');
                window.location.replace("/comments");
            }
        });
    })
});