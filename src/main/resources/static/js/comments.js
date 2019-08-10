v$(document).ready(function () {
    refreshTable();
});

function deleteComment(commentId) {
    event.preventDefault();

    console.log("COMMENT_ID: " + commentId)
    $.ajax({
        url: "/deleteComment?" + $.param({"id": commentId}),
        type: "DELETE",
        success: function (data) {
            console.log(data);
            refreshTable();
        }
    });
}

function editComment(commentId) {
    event.preventDefault();
    window.location.replace("/editComment?id=" + commentId);
}

function refreshTable() {
    $("#comments-table form").remove();
    $.ajax({
        url: "/getComments",
        type: "GET",
        success: function (data) {
            for (var i = 0, len = data.length; i < len; ++i) {
                var comment = data[i];
                $("#comments-table")
                    .append("<form class='divTableRow' id='edit-book-form'>" +
                        "<input class='book-id' type='hidden' value='" + comment.id + "'/>" +
                        "<div class='divTableCell'>" + (i + 1) + "</div>" +
                        "<div class='divTableCell'>" + comment.username + "</div>" +
                        "<div class='divTableCell'>" + comment.comment + "</div>" +
                        "<div class='divTableCell'>" + comment.books + "</div>" +
                        "<div class='divTableCell'><button id='edit-author-btn' onclick='editComment(\"" + comment.id + "\")'>Edit</button></div>" +
                        "<div class='divTableCell'><button id='delete-author-btn' onclick='deleteComment(\"" + comment.id + "\")'>Delete</button></div>" +
                        "</form>");
            }
        }
    });
}
