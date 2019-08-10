$(document).ready(function () {
    $(".create-genre-form").submit(function (event) {
        event.preventDefault();

        var genreDTO = {
            id: null,
            name: $(this).find('input.name-input').val()
        };

        console.log("genreDTO[ id: " + genreDTO.id + " | name: " + genreDTO.name + "]");

        $.ajax({
            url: "/createGenre",
            contentType : "application/json",
            data: JSON.stringify(genreDTO),
            dataType : 'text',
            type: "POST",
            success: function (data) {
                console.log(data);
                alert('genre ' + genreDTO.name + ' created')
                window.location.replace("/genres");
            }
        });
    })
});