function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

$(document).ready(function () {
    var commentId = getParameterByName('id');
    $.ajax({
        url: "/getComment?" + $.param({"id": commentId}),
        type: "GET",
        success: function (commentDataDTO) {
            console.log(commentDataDTO);
            var completeHtml = "<h1>Comment Info:</h1>" +
                "    <div class='row'>" +
                "    <input type='hidden' class='comment-id' name='book-id' value='" + commentDataDTO.id + "'/>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <label for='username-input'>Username: </label>" +
                "        <input class='username-input' id='username-input' type='text' value='" + commentDataDTO.username + "'/>" +
                "    </div>" +                "    <div class='row'>" +
                "        <label for='comment-input'>Comment: </label>" +
                "        <input class='comment-input' id='comment-input' type='text' value='" + commentDataDTO.comment + "'/>" +
                "    </div>" +
                "    <div class='box'>" +
                "        <label for='books-input'>Books: </label>" +
                "        <select class='books-input' id='books-input' size='5' multiple='multiple'>";

            var allBooks = commentDataDTO.allBooks;
            outer:
                for (var i = 0, len = allBooks.length; i < len; ++i) {
                    var books = commentDataDTO.books;
                    if (books != null) {
                        for (var j = 0, lenA = books.length; j < lenA; ++j) {
                            if (books[j] != null && allBooks[i].id == books[j].id) {
                                completeHtml += "<option selected value=\"" + allBooks[i].id + "\">" + allBooks[i].name + "</option>";
                                continue outer;
                            }
                        }
                    }
                    completeHtml += "<option value=\"" + allBooks[i].id+ "\">" + allBooks[i].name + "</option>";
                }

            completeHtml +=
                "        </select>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <button type='submit'>Save</button>" +
                "    </div>";

            $(".edit-comment-form")
                .append(completeHtml);
        }
    });

    $(".edit-comment-form").submit(function (event) {
        event.preventDefault();

        var commentCreateDTO = {
            id: $(this).find('input.comment-id').val(),
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
            url: "/editComment",
            contentType : "application/json",
            data: JSON.stringify(commentCreateDTO),
            dataType : 'text',
            type: "POST",
            success: function (data) {
                console.log(data);
                alert('comment edited');
                window.location.replace("/comments");
            }
        });
    })
});