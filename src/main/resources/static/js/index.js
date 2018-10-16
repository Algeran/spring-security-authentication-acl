$.ajax({
    url: "/countObjects",
    type:"GET",
    success:function(libraryDTO) {
        console.log(libraryDTO);
        $("#genres-count")
            .html(libraryDTO.genreCount);
        $("#authors-count")
            .html(libraryDTO.authorCount);
        $("#books-count")
            .html(libraryDTO.bookCount);
        $("#comments-count")
            .html(libraryDTO.commentCount);
    }
});