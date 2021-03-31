package view;

import entity.BloodBank;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author noura
 */
@WebServlet(name = "CreateBloodBank", urlPatterns = {"/CreateBloodBank"})
public class CreateBloodBank extends HttpServlet {
      private String errorMessage = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateBloodBank</title>"); 
            out.println("<style>");
            out.println("input[type=text] {");
            out.println("margin:8px 0;");
            out.println("width:100%;");
            out.println("height:30px;");
            out.println("border:2px solid green;");
            out.println("}");
            out.println("div {");
            out.println("border-radius:5px;");
            out.println("background-color:#f2f2f2;");
            out.println("padding:20px;");
            out.println("}");
            
            out.println("input[type=datetime-local] {");
            out.println("background-color:#C1FFC1;");
            out.println("}");
            
            out.println("input[type=submit] {");
            out.println("background-color:#4CAF50;");
            out.println("color:white;");
            out.println("padding:15px 30px;");
            out.println("margin:4px 2px;");
            out.println("}");
            out.println("</style>");
          
            
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println( "<div style=\"display: inline-block; text-align: left;\">" );
            out.println("<div>");
            out.println( "<form method=\"post\">" );
            out.println("Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",BloodBankLogic.NAME);
            out.println( "<br>" );
            out.println( "Owner Id:<br>" );
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br><br>",BloodBankLogic.OWNER_ID);
            out.println( "PrivatlyOwned:" );
            out.printf( "<input type=\"radio\" name=\"%s\" value=\"yes\" checked>Yes",BloodBankLogic.PRIVATELEY_OWNED );
            out.printf( "<input type=\"radio\" name=\"%s\" value=\"no\"> No<br><br>",BloodBankLogic.PRIVATELEY_OWNED );
            out.println("Employee Count:<br>");
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br><br>", BloodBankLogic.EMPLOYEE_COUNT );
            out.println("Date Established:<br>");
            out.printf( "<input type=\"datetime-local\" name=\"%s\" step=1 min=\"1900-01-01\" max=\"2007-12-30\"><br><br>", BloodBankLogic.ESTABLISHED);
            out.println( "<br>" );
            out.println( "<input type=\"submit\" name=\"view\" value=\"Add and View\">" );
            out.println( "<input type=\"submit\" name=\"add\" value=\"Add\">" );
            out.println( "</form>" );
            out.println("</div>");
            
            if( errorMessage != null && !errorMessage.isEmpty() ){
                out.println( "<p color=red>" );
                out.println( "<font color=red size=4px>" );
                out.println( errorMessage );
                out.println( "</font>" );
                out.println( "</p>" );
            }
            out.println( "<pre>" );
            out.println( "Submitted keys and values:" );
            out.println( toStringMap( request.getParameterMap() ) );
            out.println( "</pre>" );
            out.println( "</div>" );
            out.println( "</div>" );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }

    private String toStringMap( Map<String, String[]> values ) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
                .append( ", " )
                .append( "Value/s=" ).append( Arrays.toString( v ) )
                .append( System.lineSeparator() ) );
        return builder.toString();
    }



    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        BloodBankLogic bLogic=LogicFactory.getFor("BloodBank");
        PersonLogic personLogic=LogicFactory.getFor("Person");
        String name=request.getParameter(BloodBankLogic.NAME);
        
        if(bLogic.getBloodBankWithName(name)== null){
            //try{
                BloodBank bloodBank=bLogic.createEntity(request.getParameterMap());
                Person person=personLogic.getWithId(Integer.parseInt(request.getParameter(BloodBankLogic.OWNER_ID)));
                bloodBank.setOwner(person);
                
                String privatleyOwned=request.getParameter(BloodBankLogic.PRIVATELEY_OWNED);
                if(privatleyOwned.equals("yes")){
                    
                    bloodBank.setPrivatelyOwned(true);
                }else{
                    bloodBank.setPrivatelyOwned(false);
                }
               
                bLogic.add(bloodBank);   
                
               // }catch(Exception e){
                    //errorMessage=e.getMessage();
               // }
            //name should be unique 
               }else{
                    errorMessage = "Blood Bank : \"" + name + "\" already exists";
                }
        
      
        
        if( request.getParameter( "add" ) != null ){
            //if add button is pressed return the same page
            processRequest( request, response );
        } else if( request.getParameter( "view" ) != null ){
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect( "BloodBankTable" );
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create BloodBank Entity";
    }// </editor-fold>

}
