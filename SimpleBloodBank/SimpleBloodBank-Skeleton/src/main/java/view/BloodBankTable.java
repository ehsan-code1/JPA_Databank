package view;

import entity.BloodBank;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.Logic;
import logic.LogicFactory;

/**
 *
 * @author Nouran Nouh
 */
@WebServlet(name = "BloodBankTable", urlPatterns = {"/BloodBankTable"})
public class BloodBankTable extends HttpServlet {

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
            out.println("<title>Servlet BloodBankTable</title>");
            //style 
            out.println("<style>");
            out.println("table {");
            out.println("border-collapse: collapse;");
            out.println("width:100%;");
            out.println("}"); 
            out.println("th,td {");
            out.println("padding:8px;");
            out.println("text-align:left;");
            out.println("border-bottom: 1px solid #ddd;");
            out.println("}"); 
            
            out.println("th {");
            out.println("background-color: #4CAF50;");
            out.println("color: white;");
            out.println("}");
            
            out.println("tr:hover {");
            out.println("background-color:#f5f5f5;");
            out.println("}"); 
           
            //end style
            out.println("</style>");
            
            out.println("</head>");
            out.println("<body>");
            out.println("<h2 style=\"text-align:center\"> Blood Bank </h2>");
            out.println( "<table>" );
           // out.println( "<caption>Blood Bank</caption>" );
            
            Logic<BloodBank> logic = LogicFactory.getFor( "BloodBank" );
            out.println( "<tr>" );
            logic.getColumnNames().forEach( c -> out.printf( "<th>%s</th>", c ) );
            out.println( "</tr>" );
            logic.getAll().forEach( e -> out.printf( "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                logic.extractDataAsList( e ).toArray() ) );        
            
            out.println("</body>");
            out.println("</html>");
        }
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
            
        BloodBankLogic logic=LogicFactory.getFor("BloodBank");
        BloodBank bloodBank=logic.updateEntity(request.getParameterMap());
        logic.update(bloodBank);
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
