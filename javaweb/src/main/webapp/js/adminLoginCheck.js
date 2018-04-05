$(window).on("load",()=>{
    if(window!=top)(
        top.location.href="adminLogin.html"
    )

    $("#btnLogin").click(()=>{
        var username = $.trim($("#username").val())
        var password = $.trim($("#password").val())

        if(!username){
            layer.alert("姓名不能为空！")
            return
        }
        if(!password){
            layer.alert("密码不能为空！")
            return
        }

        $.ajax({
            type:"post",
            url:"AdminServlet",
            data:{username:username,password:password},
            dataType:"json",
            success:function (data) {
                if(data && data.username){

                    location.href="adminList.html"
                }else{
                    layer.alert("错误的用户名或者密码"+data.code+data.message)//????????拿不到data值
                }
            }
        })
    })

})