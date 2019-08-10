$(document).ready(function () {
    refreshTable();
});

function deleteBook(bookId) {
    event.preventDefault();

    console.log("BOOK_ID: " + bookId)
    $.ajax({
        url: "/deleteBook?" + $.param({"id": bookId}),
        type: "DELETE",
        success: function (data) {
            console.log(data);
            refreshTable();
        }
    });
}

function editBook(bookId) {
    event.preventDefault();
    window.location.replace("/editBook?id=" + bookId);
}

function refreshTable() {
    $("#books-table form").remove();
    $.ajax({
        url: "/getBooks",
        type: "GET",
        success: function (data) {
            for (var i = 0, len = data.length; i < len; ++i) {
                var book = data[i];
                $("#books-table")
                    .append("<form class='divTableRow' id='edit-book-form'>" +
                        "<input class='book-id' type='hidden' value='" + book.id + "'/>" +
                        "<div class='divTableCell'>" + (i + 1) + "</div>" +
                        "<div class='divTableCell'>" + book.name + "</div>" +
                        "<div class='divTableCell'>" + book.date + "</div>" +
                        "<div class='divTableCell'>" + book.authors + "</div>" +
                        "<div class='divTableCell'>" + book.genre + "</div>" +
                        "<div class='divTableCell'><button id='edit-author-btn' onclick='editBook(\"" + book.id + "\")'>Edit</button></div>" +
                        "<div class='divTableCell'><button id='delete-author-btn' onclick='deleteBook(\"" + book.id + "\")'>Delete</button></div>" +
                        "</form>");
            }
        }
    });
}
