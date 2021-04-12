<%-- 
    Document   : CreateBloodBankJsp
    Created on : Apr. 11, 2021, 6:31:56 p.m.
    Author     : noura
--%>

<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Create blood bank</title>
        <style>
            input[type=text]{
                margin:8px 0;
                width:100%;
                height:30px;
                border:2px solid green;
            }
            div{
                border-radius: 5px;
                background-color: #C1FFC1;
                
                
            }
             input[type=submit]{
                background-color:#4CAF50;
                color:white;
                padding:15px 30px;
                margin:4px 2px;
            }
            
            
        </style>
    </head>
    <body>
      <div style=\"text-align: center;\">
          <div style=\"display: inline-block; text-align: left;\">
              <div>
                  <form name= "bloodbank" method="post" action="CreateBloodBankJSP">
                      Name: <span id="error-name"></span><br>
                      <input type="text" name="name"/><br>
                      Owner id:<br>
                      <input type="text" name="owner_id"/><br>
                      Privately Owned:<br>
                      <select name="privately_owned">
                          <option value="yes">Yes</option>
                          <option value="no">No</option>
                      </select>
                      <br>
                      
                      Employee Count:<span id="error-employee"></span> <br>
                      <input type="text" name="employee_count"/><br>
                      Date Established:<br>
                      <input type="datetime-local" name="established" step=1 min="1900-01-01" max="2007-12-30"><br><br>
                      <br>
                      <input type="submit" name="view" value="Add and view">
                      <input type="submit" name="add" value="Add">
                      
                     
                  </form>
              </div>  
          </div>
      </div>
        <script>
            function validateForm(){
                var name=document.forms["bloodbank"]["name"].value;
                var employeeCount=document.forms["bloodbank"]["employee_count"].value;
                if(name.length<1){
                     document.getElementById('error-name').innerHTML="name is required"
                }
                
                 if(employeeCount.length===0){
                     document.getElementById('error-employee').innerHTML="employee count is required"
                }
    
          }
        </script>
            
            
    </body>
</html>
