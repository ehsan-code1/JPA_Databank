package view;

import entity.BloodDonation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodDonationLogic;
import logic.Logic;
import logic.LogicFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ehsan
 */

@WebServlet(name = "BloodDonationTable", urlPatterns = {"/BloodDonationTable"})

public class BloodDonationTableView extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>BloodDonationView</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">");
            out.println("<caption>BloodDonation</caption>");
            Logic<BloodDonation> logic = LogicFactory.getFor("BloodDonation");

            out.println("<tr>");

            logic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));

            out.println("</tr>");

            logic.getAll().forEach(e -> out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    logic.extractDataAsList(e).toArray()));

            out.println("<tr>");
            logic.getColumnNames().forEach(c -> out.printf("<th>%s</th>", c));
            out.println("</tr>");
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        m.keySet().forEach(k -> {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        });
        return builder.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            log("POST");
            BloodDonationLogic logic = LogicFactory.getFor("BloodDonation");
            BloodDonation bd = logic.updateEntity(request.getParameterMap());
            logic.update(bd);
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(BloodDonationTableView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Sample of BloodDonation View Normal";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            log("GET");
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(BloodDonationTableView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final boolean DEBUG = true;

    @Override
    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    @Override
    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
