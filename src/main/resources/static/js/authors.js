$(document).ready(function () {
    refreshTable();
});

function deleteAuthor(authorId) {
    event.preventDefault();

    console.log("AUTHOR_ID: " + authorId)
    $.ajax({
        url: "/deleteAuthor?" + $.param({"id": authorId}),
        type: "DELETE",
        success: function (data) {
            console.log(data);
            refreshTable();
        }
    });
}

function refreshTable() {
    $("#authors-table form").remove();
    $.ajax({
        url: "/getAuthors",
        type: "GET",
        success: function (data) {
            for (var i = 0, len = data.length; i < len; ++i) {
                var author = data[i];
                $("#authors-table")
                    .append("<form class='divTableRow' id='edit-author-form'>" +
                        "<input class='author-id' type='hidden' value='" + author.id + "'/>" +
                        "<div class='divTableCell'>" + (i + 1) + "</div>" +
                        "<div class='divTableCell'><input class='author-name-input' name='namer' type='text' value='" + author.name + "'/></div>" +
                        "<div class='divTableCell'><input class='author-surname-input' name='surnamer' type='text' value='" + author.surname + "'/></div>" +
                        "<div class='divTableCell'><input class='author-country' readonly='readonly' type='text' value='" + author.country + "'/></div>" +
                        "<div class='divTableCell'><button type='submit' id='edit-author-btn'>Edit</button></div>" +
                        "<div class='divTableCell'><button id='delete-author-btn' onclick='deleteAuthor(\"" + author.id + "\")'>Delete</button></div>" +
                        "</form>");
            }

            $('.divTableRow').submit(function (event) {
                event.preventDefault();

                var authorDTO = {
                    id: $(this).find('input.author-id').val(),
                    name: $(this).find('input.author-name-input').val(),
                    surname: $(this).find('input.author-surname-input').val()
                };
                console.log("authorDTO[ id: " + authorDTO.id + " | name: " + authorDTO.name + " | surname: " + authorDTO.surname + "]");
                $.ajax({
                    url: "/editAuthor",
                    contentType : "application/json",
                    data: JSON.stringify(authorDTO),
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
