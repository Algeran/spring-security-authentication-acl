$(document).ready(function () {
    $(".create-author-form div").remove()

    $.ajax({
        url: "/getCountries",
        type: "GET",
        success: function (data) {
            console.log("data " + data);

            var completeHtml = "<h1>Create author:</h1>" +
                "    <div class=\"row\">" +
                "        <label for=\"name-input\">Name: </label>" +
                "        <input id=\"name-input\" class=\"name-input\" name=\"name\" type=\"text\"/>" +
                "    </div>" +
                "    <div class=\"row\">" +
                "        <label for=\"surname-input\">Surname: </label>" +
                "        <input id=\"surname-input\" class=\"surname-input\" name=\"surname\" type=\"text\"/>" +
                "    </div>" +
                "    <div class=\"row\">" +
                "        <label for=\"country-input\">Country:</label>" +
                "        <select id=\"country-input\" class='country-input' name=\"country\">";

            for (var i = 0, len = data.length; i < len; ++i) {
                    completeHtml += "<option value=\"" + data[i] + "\">" + data[i] + "</option>";
            }

            completeHtml += "</select>" +
                "    </div>" +
                "<div class=\"row\">" +
                "    <button type=\"submit\">Save</button>" +
                "</div>";

            $(".create-author-form")
                .append(completeHtml);
        }
    });
    
    $(".create-author-form").submit(function (event) {
        event.preventDefault();

        var authorDTO = {
            id: null,
            name: $(this).find('input.name-input').val(),
            surname: $(this).find('input.surname-input').val(),
            country: $(this).find('select.country-input').val()
        };

        console.log("authorDTO[ id: " + authorDTO.id + " | name: " + authorDTO.name +
            "surname: " + authorDTO.surname + " | country: " + authorDTO.country + "]");

        $.ajax({
            url: "/createAuthor",
            contentType : "application/json",
            data: JSON.stringify(authorDTO),
            dataType : 'text',
            type: "POST",
            success: function (data) {
                console.log(data);
                alert('author ' + authorDTO.name + ' ' + authorDTO.surname + ' created')
                window.location.replace("/authors");
            }
        });
    })
});

function getCountries() {
    return
}