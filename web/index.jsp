<%--
  Created by IntelliJ IDEA.
  User: zzde
  Date: 2018/10/30
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  $END$
  </body>

  <script>
      if(window.WebSocket) {
          socket = new WebSocket("ws://localhost:3333")
          console.log(socket)
      }

      const data = {
          "type" : "test"
      };

      socket.onopen = setTimeout(function(event){
          console.log("WebSocket已成功连接！");
      }, 1000)

  </script>
</html>
