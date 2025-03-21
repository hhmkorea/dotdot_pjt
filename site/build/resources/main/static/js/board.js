let index = {
    init: function () {
        $("#btn-save").on("click", () => {
            this.save();
        });
        $("#btn-delete").on("click", () => {
            this.deleteById();
        });
        $("#btn-update").on("click", () => {
            this.update();
        });
        $("#btn-reply-save").on("click", () => {
            this.replySave();
        });
    },

    save: function () {
        if(!confirm("저장 하시겠습니까?")) {
            return false;
        }

        let data = {
            title: $("#title").val(),
            content: $("#content").summernote('code')
        };
        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            alert("글쓰기가 완료되었습니다.");
            location.href = "/board/list";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    deleteById: function () {
        if(!confirm("삭제 하시겠습니까?")) {
            return false;
        }

        let id = $("#id").text();
        $.ajax({
            type: "DELETE",
            url: "/api/board/" + id,
            dataType: "json"
        }).done(function (resp){
            alert("삭제가 완료되었습니다.");
            location.href = "/board/list";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        if(!confirm("수정 하시겠습니까?")) {
            return false;
        }

        let id = $("#id").val();

        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };
        $.ajax({
            type: "PUT",
            url: "/api/board/" + id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            alert("글수정이 완료되었습니다.");
            location.href = "/board/" + id;
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    replySave: function () {
        let data = {
            userId: $("#userId").val(),
            boardId : $("#boardId").val(),
            content: $("#reply-content").val()
        };

        console.log(data);

        $.ajax({
            type: "POST",
            url: `/api/board/${data.boardId}/reply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            alert("댓글 작성이 완료되었습니다.");
            location.href = `/board/${data.boardId}`;
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    replyDelete: function (boardId, replyId) {

        $.ajax({
            type: "DELETE",
            url: `/api/board/${boardId}/reply/${replyId}`,
            dataType: "json"
        }).done(function (resp){
            alert("댓글이 삭제되었습니다.");
            location.href = `/board/${boardId}`;
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },
}

index.init();

// 소스 출처 : https://github.com/codingspecialist/Springboot-JPA-Blog