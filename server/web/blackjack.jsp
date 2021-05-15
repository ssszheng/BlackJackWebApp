<%-- 
    Document   : blackjack
    Created on : May 7, 2021, 6:18:04 PM
    Author     : szheng
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="nz.ac.massey.cs.webtech.s_20010847.server.*"%>
<%@page import="org.json.JSONArray "%> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BlackJack</title>
    </head>
    <body>
      
         <% 
          String url = request.getRequestURL().toString();
          if (!url.contains("jsessionid")){
              RequestDispatcher rd = request.getRequestDispatcher("/start");
              rd.forward(request, response);
          }else {
          String sessionid = url.split("jsessionid=")[1];%>
          
          
          
         
         <%
           Game game = (Game)session.getAttribute(sessionid);
           if (game != null){
                List<Card> user = game.getUserCards();
                List<Card> ai = game.getAICards();
                
                
         %>
        
        <p id="sessionid" style="display:none;"><%=sessionid%></p>
        
        <p>User Hand: </p>
               
        <% JSONArray userList = new JSONArray();%>
        
        
         <%Iterator<Card> iterUser = user.iterator();
            while(iterUser.hasNext()){
               
               Card c = iterUser.next();
               Object s = c.suite;
               Object v = c.value;
               out.print(s+" ");
               out.print(v+",");
               userList.put(c.toString());
               
         } %>
          
        <p id="userJson" style="display:none;"><%=userList%></p>
        
        <br>     
        <canvas id="userCanvas" width="700" height="200" style="border:1px solid #d3d3d3;"> Your browser does not support the HTML5 canvas tag.</canvas>
               
               
               
        <p>Computer Hand: </p>
        <% JSONArray aiList = new JSONArray();%>
        
        <% Iterator<Card> iterAI = ai.iterator();
           while(iterAI.hasNext()){
               Card c = iterAI.next();
               Object s = c.suite;
               Object v = c.value;
               out.print(s+" ");
               out.print(v+",");
               aiList.put(c.toString());
        } %>
        
      
        
       <p id="aiJson" style="display:none;"><%= aiList %></p>
        
        
       <%}}%>
        
        
         
        
        <br>
        <canvas id="aiCanvas" width="700" height="200" style="border:1px solid #d3d3d3;"> Your browser does not support the HTML5 canvas tag.</canvas>

        
             
        <br>
        <div>
        <button type="button"  onclick="javascript:callStartServlet()">Start New Game</button>
        <button type="button"  onclick="javascript:callHitServlet()">Hit</button>
        <button type="button"  onclick="javascript:callStandServlet()">Stand</button>
        <button type="button"  onclick="javascript:callStateServlet()">State(current game)</button>
        <br><br>
        <button type="button"  onclick="javascript:callPossibleMovesServlet()">Possible Moves</button>
        <button type="button"  onclick="javascript:callStatsServlet()">Stats(all games)</button>
        

        </div>
        
        <script type="text/javascript">
        var sessionid = ";jsessionid="+ document.getElementById("sessionid").innerText;
        
        function callStartServlet() {
            var form = document.createElement('form');
            form.setAttribute('method', 'post');
            form.setAttribute('action', '/jack/start');
            form.style.display = 'hidden';
            document.body.appendChild(form);
            form.submit();

        }
        function callHitServlet() {
            var form = document.createElement('form');
            form.setAttribute('method', 'post');
            form.setAttribute('action', '/jack/move/hit'+sessionid);
            form.style.display = 'hidden';
            document.body.appendChild(form);
            form.submit();
            
        }
        
        function callStandServlet() {
            
            var form = document.createElement('form');
            form.setAttribute('method', 'post');
            form.setAttribute('action', '/jack/move/stand'+sessionid);
            form.style.display = 'hidden';
            document.body.appendChild(form);;
            form.submit();
            
        }
        function callStateServlet() {
            document.location.href="/jack/state"+sessionid;
        }
        
        function callPossibleMovesServlet() {
            document.location.href="/jack/possiblemoves"+sessionid;
        }
        function callStatsServlet() {
            document.location.href="/jack/stats";
        }
        
        function strokeCard(ctx, x, y) {
            ctx.beginPath();
            ctx.rect(x, y, 100, 150);
            ctx.stroke();
        }
        
        window.onload = function() {
            var url = window.location.href;
            var path = url.split("jack")[0] + "jack"; //localhost/jack

            var club = new Image();   // Create new img element
            club.src = path + '/images/club.png'; // Set source path

            var diamond = new Image();   
            diamond.src = path +'/images/diamond.png'; 

            var heart = new Image();   
            heart.src = path +'/images/heart.png'; 

            var spade = new Image();   
            spade.src = path +'/images/spade.png'; 
            //user hand 
            var ucanvus = document.getElementById("userCanvas");
            var uctx = ucanvus.getContext("2d");
            uctx.font = "20px Georgia";

            var userJSarray = document.getElementById("userJson").innerText;
            var userObj = JSON.parse(userJSarray);
        
            for (i = 0 ;i < userObj.length; i++){
                var suite = userObj[i].split(":")[0];
                var value = userObj[i].split(":")[1];
                strokeCard(uctx, 20 + i*130, 20);
                uctx.strokeText(value, 30 + i*130, 40);
                if (suite === "CLUBS"){
                    uctx.drawImage(club, 40 + i*130, 60,60,60);
                }
                else if (suite === "DIAMONDS"){
                    uctx.drawImage(diamond, 40 + i*130, 60,60,60);
                }
                else if (suite === "HEARTS"){
                    uctx.drawImage(heart, 40 + i*130, 60,60,60);
                }
                else if (suite === "SPADES"){
                    uctx.drawImage(spade, 40 + i*130, 60,60,60);
                }
            }
        

//        document.getElementById("message").innerHTML = sessionid;
  
        
            //ai hand 
            var aiJSarray = document.getElementById("aiJson").innerText;
            var aiObj = JSON.parse(aiJSarray);

            var aic = document.getElementById("aiCanvas");
            var actx = aic.getContext("2d");
            actx.font = "20px Georgia";
            actx.beginPath();
            actx.rect(20, 20, 100, 150);

            actx.strokeText("?", 65, 100);
            actx.rect(40, 40, 60, 110);      
            actx.stroke();

      
            for (i = 1 ;i < aiObj.length; i++){
                var suite = aiObj[i].split(":")[0];
                var value = aiObj[i].split(":")[1];
                strokeCard(actx, 20 + i*130, 20);
                actx.strokeText(value, 30 + i*130, 40);
                if (suite === "CLUBS"){
                    actx.drawImage(club, 40 + i*130, 60,60,60);
                }
                else if (suite === "DIAMONDS"){
                    actx.drawImage(diamond, 40 + i*130, 60,60,60);
                }
                else if (suite === "HEARTS"){
                    actx.drawImage(heart, 40 + i*130, 60,60,60);
                }
                else if (suite === "SPADES"){
                    actx.drawImage(spade, 40 + i*130, 60,60,60);
                }
           }  };      
        
       </script>

        
    </body>
</html>
