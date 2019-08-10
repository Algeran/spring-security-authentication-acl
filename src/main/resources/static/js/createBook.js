$(document).ready(function () {
    $.ajax({
        url: "/getBookData",
        type: "GET",
        success: function (bookDataDTO) {
            console.log(bookDataDTO);
            var completeHtml = "<h1>Book Info:</h1>" +
                "" +
                "    <div class='row'>" +
                "        <label for='name-input'>Name: </label>" +
                "        <input class='name-input' id='name-input' type='text'/>" +
                "    </div>" +
                "" +
                "    <div class='row'>" +
                "        <label for='date-input'>Published date: </label>" +
                "        <input class='date-input' id='date-input' type='date'/>" +
                "    </div>" +
                "" +
                "    <div class='box'>" +
                "        <label for='authors-input'>Authors: </label>" +
                "        <select class='authors-input' id='authors-input' size='5' multiple='multiple'>";
            var allAuthors = bookDataDTO.allAuthors;
            for (var i = 0, len = allAuthors.length; i < len; ++i) {
                completeHtml += "<option value='" + allAuthors[i].id+ "'>" + allAuthors[i].name + " " + allAuthors[i].surname + "</option>";
            }
            completeHtml +=
                "        </select>" +
                "    </div>" +
                "    <div class='box'>" +
                "        <label for='genre-input'>Genre: </label>" +
                "        <select class='genre-input' id='genre-input'>";
            var allGenres = bookDataDTO.allGenres;
            for (var j = 0, lenj = allGenres.length; j < lenj; ++j) {
                completeHtml += "<option id='genre-input' class='genre-input' value='" + allGenres[j].id + "'>" + allGenres[j].name + "</option>";
            }
            completeHtml +=
                "        </select>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <button type='submit' name='action' value='save'>Save</button>" +
                "    </div>";

            $(".create-book-form")
                .append(completeHtml);
        }
    });

    $(".create-book-form").submit(function (event) {
        event.preventDefault();

        var bookCreateDTO = {
            id: null,
            name: $(this).find('input.name-input').val(),
            date: $(this).find('input.date-input').val(),
            authors: $(this).find('select.authors-input').val(),
            genre: $(this).find('select.genre-input').val()
        };

        console.log("bookDTO[name: " + bookCreateDTO.name +
            ", date: " + bookCreateDTO.date +
            ", authors: " + bookCreateDTO.authors +
            ", genre: " + bookCreateDTO.genre +
            "]");

        $.ajax({
            url: "/createBook",
            contentType : "application/json",
            data: JSON.stringify(bookCreateDTO),
            dataType : 'text',
            type: "POST",
            success: function (data) {
                console.log(data);
                alert('book ' + bookCreateDTO.name + ' created')
                window.location.replace("/books");
            }
        });
    })
});