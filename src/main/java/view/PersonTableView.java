package view;

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
import logic.Logic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Abdul
 */
@WebServlet( name = "PersonTable", urlPatterns = { "/PersonTable" } )
public class PersonTableView extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        response.setContentType( "text/html;charset=UTF-8" );
        try( PrintWriter out = response.getWriter() ) {
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<title>PersonViewNormal</title>" );
            out.println( "</head>" );
            out.println( "<body>" );
            /**
             * this is an example, for your other tables use getColumnNames from
             * logic to create headers in a loop.
             */
            out.println( "<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">" );
            out.println( "<caption>Person</caption>" );
            /**
             * PersonLogin logic = LogicFactory.getFor( "Person" );
             */
            Logic<Person> logic = LogicFactory.getFor( "Person" );
            out.println( "<tr>" );
            logic.getColumnNames().forEach( c -> out.printf( "<th>%s</th>", c ) );
            out.println( "</tr>" );
            /**
             * this is get all the attributes in the database
             */
            logic.getAll().forEach( e -> out.printf( "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    logic.extractDataAsList( e ).toArray() ) );

            out.println( "<tr>" );
            logic.getColumnNames().forEach( c -> out.printf( "<th>%s</th>", c ) );
            out.println( "</tr>" );
            out.println( "</table>" );
            out.printf( "<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap( request.getParameterMap() ) );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }
    private String toStringMap( Map<String, String[]> m ) {
        StringBuilder builder = new StringBuilder();
        for( String k: m.keySet() ) {
            builder.append( "Key=" ).append( k )
                    .append( ", " )
                    .append( "Value/s=" ).append( Arrays.toString( m.get( k ) ) )
                    .append( System.lineSeparator() );
        }
        return builder.toString();
    }
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
     @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "GET" );
        processRequest( request, response );
    }
    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "POST" );
        PersonLogic logic = LogicFactory.getFor( "Person" );
        Person person = logic.updateEntity( request.getParameterMap() );
        logic.update( person );
        processRequest( request, response );
    }
     /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Person View Normal";
    }
    private static final boolean DEBUG = true;

    public void log( String msg ) {
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message );
        }
        
    }
       public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }
}


