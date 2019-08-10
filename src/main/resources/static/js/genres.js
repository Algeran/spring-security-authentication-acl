$(document).ready(function () {
    refreshTable();
});

function deleteGenre(genreId) {
    event.preventDefault();
    console.log("GENRE_ID: " + genreId)
    $.ajax({
        url: "/deleteGenre?" + $.param({"id": genreId}),
        type: "DELETE",
        success: function (data) {
            console.log(data);
            refreshTable();
        }
    });
}

function refreshTable() {
    $("#genres-table form").remove();
    $.ajax({
        url: "/getGenres",
        type: "GET",
        success: function (data) {
            for (var i = 0, len = data.length; i < len; ++i) {
                var genre = data[i];
                $("#genres-table")
                    .append("<form class='divTableRow' id='edit-genre-form'>" +
                            "<input class='genre-id' type='hidden' value='" + genre.id + "'/>" +
                            "<div class='divTableCell'>" + (i + 1) + "</div>" +
                            "<div class='divTableCell'><input class='genre-name-input' name='namer' type='text' value='" + genre.name + "'/></div>" +
                            "<div class='divTableCell'><button type='submit' id='edit-genre-btn'>Edit</button></div>" +
                            "<div class='divTableCell'><button id='delete-genre-btn' onclick='deleteGenre(\"" + genre.id + "\")'>Delete</button></div>" +
                        "</form>");
            }

            $('.divTableRow').submit(function (event) {
                event.preventDefault();

                var genreDTO = {
                    id: $(this).find('input.genre-id').val(),
                    name: $(this).find('input.genre-name-input').val()
                };
                console.log("genreDTO[ id: " + genreDTO.id + " | name: " + genreDTO.name + "]");
                $.ajax({
                    url: "/editGenre",
                    contentType : "application/json",
                    data: JSON.stringify(genreDTO),
                    dataType : 'text',
                    type: "POST",
                    success: function (data) {
                        console.log(data);
                        refreshTable();
                    }
                });
            })
        }
    });
}
