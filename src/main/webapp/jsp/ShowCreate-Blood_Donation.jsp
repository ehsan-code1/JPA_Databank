<%-- 
    Document   : ShowTable-Account
    Created on : Jan 4, 2020
    Author     : Shariar (Shawn) Emami
--%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    </head>
    <body>
        <div style="margin-left: 400px  ">

            <form  method="post">
                Blood Bank ID:<br>  
                <input type="number" name= "bank_id" value=""><br>
                <br>
                Milliliters:
                <br>
                <input type="number" name="milliliters" value=""><br>     
                <br>
                Blood Group:
                <select name="blood_group" style="margin-left:35px">
                    <option value="A">A</option>
                    <option value="B">B</option>
                    <option value="AB">AB</option>
                    <option value="O">O</option>
                </select>
                <br>
                <br>
                RHD:
                <select name="rhesus_factor" style="margin-left:85px"> 
                    <option value="Positive">Positive</option>
                    <option value="Negative">Negative</option>
                </select>
                <br>
                <br>
                Created:<br>
                <input type="datetime-local" step="1" name="created" value="">
                <br>
                <br>
                <input type="submit" name="view" value="Add and View">
                <input type="submit" name="add" value="Add">
            </form>
        </div>
    </body>
</html>