let index = {
    init: function () {
        $("#btn-save-item").on("click", () => {
            this.save();
        });
        $("#btn-delete-item").on("click", () => {
            this.deleteById();
        });
        $("#btn-update-item").on("click", () => {
            this.update();
        });
    },

    save: function () {
        if(!confirm("저장 하시겠습니까?")) {
            return false;
        }

        let data = {
            id: $("#id").val(),
            name: $("#name").val(),
            price: $("#price").val(),
            stockQuantity: $("#stockQuantity").val(),
            author: $("#author").val(),
            itemDetail: $("#itemDetail").val(),
            itemSellStatus: $("#itemSellStatus").val()
        };
        $.ajax({
            type: "POST",
            url: "/api/items/new",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            alert("상품등록이 완료되었습니다.");
            location.href = "/shop/main";
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
            url: "/api/items/" + id,
            dataType: "json"
        }).done(function (resp){
            alert("삭제가 완료되었습니다.");
            location.href = "/shop/main";
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
            name: $("#name").val(),
            price: $("#price").val(),
            stockQuantity: $("#stockQuantity").val(),
            author: $("#author").val(),
            itemDetail: $("#itemDetail").val(),
            itemSellStatus: $("#itemSellStatus").val()
        };
        $.ajax({
            type: "PUT",
            url: "/api/items/" + id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            alert("글수정이 완료되었습니다.");
            location.href = "/shop/main";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

}

index.init();

// 소스 출처 : https://github.com/codingspecialist/Springboot-JPA-Blog