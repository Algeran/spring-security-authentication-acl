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
    var bookId = getParameterByName('id');
    $.ajax({
        url: "/getBook?" + $.param({"id": bookId}),
        type: "GET",
        success: function (bookDataDTO) {
            console.log(bookDataDTO);
            var completeHtml = "<h1>Book Info:</h1>" +
                "    <div class='row'>" +
                "    <input type='hidden' class='book-id' name='book-id' value='" + bookDataDTO.id + "'/>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <label for='name-input'>Name: </label>" +
                "        <input class='name-input' id='name-input' type='text' value='" + bookDataDTO.name + "'/>" +
                "    </div>" +
                "    <div class='row'>" +
                "        <label for='date-input'>Published date: </label>" +
                "        <input class='date-input' id='date-input' type='date' value='" + bookDataDTO.date + "'/>" +
                "    </div>" +
                "" +
                "    <div class='box'>" +
                "        <label for='authors-input'>Authors: </label>" +
                "        <select class='authors-input' id='authors-input' size='5' multiple='multiple'>";

            var allAuthors = bookDataDTO.allAuthors;
            outer:
            for (var i = 0, len = allAuthors.length; i < len; ++i) {
                var authors = bookDataDTO.authors;
                if (authors != null) {
                    for (var j = 0, lenA = authors.length; j < lenA; ++j) {
                        if (allAuthors[i].id == authors[j].id) {
                            completeHtml += "<option selected value=\"" + allAuthors[i].id + "\">" + allAuthors[i].name + " " + allAuthors[i].surname + "</option>";
                            continue outer;
                        }
                    }
                }
                completeHtml += "<option value=\"" + allAuthors[i].id+ "\">" + allAuthors[i].name + " " + allAuthors[i].surname + "</option>";
            }

            completeHtml +=
                "        </select>" +
                "    </div>" +
                "    <div class='box'>" +
                "        <label for='genre-input'>Genre: </label>" +
                "        <select class='genre-input' id='genre-input' >";

            var allGenres = bookDataDTO.allGenres;
            for (var d = 0, lenD = allGenres.length; d < lenD; ++d) {
                    if (bookDataDTO.genre != null && allGenres[d].id == bookDataDTO.genre.id) {
                        completeHtml += "<option id='genre-input' class='genre-input' selected='selected' value=\"" + allGenres[d].id + "\">" + allGenres[d].name + "</option>";
                    } else {
                        completeHtml += "<option id='genre-input' class='genre-input' value=\"" + allGenres[d].id + "\">" + allGenres[d].name + "</option>";
                    }
            }
            completeHtml +=
                "        </select>" +
                "    </div>" +
                "" +
                "    <div class='row'>" +
                "        <button type='submit'>Save</button>" +
                "    </div>";

            $(".edit-book-form")
                .append(completeHtml);
        }
    });

    $(".edit-book-form").submit(function (event) {
        event.preventDefault();

        var bookCreateDTO = {
            id: $(this).find('input.book-id').val(),
            name: $(this).find('input.name-input').val(),
            date: $(this).find('input.date-input').val(),
            authors: $(this).find('select.authors-input').val(),
            genre: $(this).find('select.genre-input').val()
        };

        console.log("bookDTO[ id: " + bookCreateDTO.id +
            ", name: " + bookCreateDTO.name +
            ", date: " + bookCreateDTO.date +
            ", authors: " + bookCreateDTO.authors +
            ", genre: " + bookCreateDTO.genre +
            "]");

        $.ajax({
            url: "/editBook",
            contentType : "application/json",
            data: JSON.stringify(bookCreateDTO),
            dataType : 'text',
            type: "POST",
            success: function (data) {
                console.log(data);
                alert('book ' + bookCreateDTO.name + ' edited')
                window.location.replace("/books");
            }
        });
    })
});