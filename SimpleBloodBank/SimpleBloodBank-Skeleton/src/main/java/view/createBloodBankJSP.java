package view;

import entity.BloodBank;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.RequestDispatcher;
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
 * @author Nouran Nouh
 */
@WebServlet(name = "CreateBloodBankJSP", urlPatterns = {"/CreateBloodBankJSP"})
public class createBloodBankJSP extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
        private void fillTableData(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.setAttribute("request", toStringMap(req.getParameterMap()));
        req.setAttribute("path", path);
        req.setAttribute("title", path.substring(1));
        req.getRequestDispatcher("jsp/CreateBloodBankJsp.jsp").forward(req, resp);

    }
        
    private String toStringMap(Map<String, String[]> m) {
    StringBuilder builder = new StringBuilder();
    m.keySet().forEach((k) -> {
    builder.append("Key=").append(k)
            .append(", ")
            .append("Value/s=").append(Arrays.toString(m.get(k)))
            .append(System.lineSeparator());
    });
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
        
            fillTableData(request, response);
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
        //get required entity logic 
        BloodBankLogic bLogic=LogicFactory.getFor("BloodBank");
        PersonLogic personLogic=LogicFactory.getFor("Person");
        
        String name=request.getParameter(BloodBankLogic.NAME);
        
        if(bLogic.getBloodBankWithName(name)== null){
            //try{
                BloodBank bloodBank=bLogic.createEntity(request.getParameterMap());
                
                //if users enters no owner_id 
                //owner id can be null
                String owner=request.getParameter(BloodBankLogic.OWNER_ID) ;
                if(owner.isEmpty()){
                    bloodBank.setOwner(null);
                
                } else{ 
                    //set owner id 
                    Person person=personLogic.getWithId(Integer.parseInt(owner));
                    bloodBank.setOwner(person);
                } 
                
                //get privately owned 
                 String privatleyOwned=request.getParameter(BloodBankLogic.PRIVATELY_OWNED);
                 //if it is yes 
                if(privatleyOwned.equals("yes")){
                    //set it to true
                    bloodBank.setPrivatelyOwned(true);
                }else{
                    //set it to yes
                    bloodBank.setPrivatelyOwned(false);
                }
               
                bLogic.add(bloodBank);
                
        }
        
            if( request.getParameter( "add" ) != null ){
            //if add button is pressed return the same page
                PrintWriter out = response.getWriter();
                out.print("Values added :");
                out.println(toStringMap(request.getParameterMap()));
        } else if( request.getParameter( "view" ) != null ){
            //if view button is pressed redirect to the appropriate table (jsp)
             response.sendRedirect( "BloodBankTableJSP" );
        }
        
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
