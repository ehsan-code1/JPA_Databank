package view;

import common.ValidationException;
import entity.BloodBank;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.LogicFactory;
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.PersonLogic;

/**
 *
 * @author Ehsan
 */
@WebServlet(name = "BloodDonationForm", urlPatterns = {"/DonateBloodFrom"})
public class BloodFormView extends HttpServlet {

    private String errorMessage = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create BloodDonation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: left; display:block\">");
            out.println("<form id =\"inputForm\" method=\"post\">");

            out.println("<div style=\"margin-top:50px;background-color: lightblue;padding:20px;border-radius:15px;position: relative;\">");

            out.println("BloodBank ID:<br>");
            //instead of typing the name of column manualy use the static vraiable in logic
            //use the same name as column id of the table. will use this name to get date
            //from parameter map.
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANKID);
            out.println("<br>");

            out.println("Milliliters:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");

            out.println("Blood Group:");
            out.printf("<select name=\"%s\" style=\"margin-left:35px\">"
                    + "  <option value=\"A\">A</option>"
                    + "  <option value=\"B\">B</option>"
                    + "  <option value=\"AB\">AB</option>"
                    + "  <option value=\"O\">O</option></select>", BloodDonationLogic.BLOOD_GROUP);
            out.println("<br>");
            out.println("<br>");

            out.println("RHD:");
            out.printf("<select name=\"%s\" style=\"margin-left:85px\">"
                    + "<option value=\"Positive\">Positive</option>"
                    + "  <option value=\"Negative\">Negative</option></select>", BloodDonationLogic.RHESUS_FACTOR);
            out.println("<br>");
            out.println("<br>");

            out.println("Created:<br>");
            out.printf("<input type=\"datetime-local\" step=\"1\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.CREATED);
            out.println("<br>");
            out.println("</div>");

            out.println("<div style=\"margin-top:50px;background-color: lightblue;padding:20px;border-radius:15px; postion:relative; \">");

            out.println("FirstName:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.FIRST_NAME);
            out.println("<br>");
            out.println("LastName:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.LAST_NAME);
            out.println("<br>");
            out.println("Phone:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.PHONE);
            out.println("<br>");
            out.println("Address:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.ADDRESS);
            out.println("<br>");
            out.println("Birth:<br>");
            out.printf("<input type=\"date\" name=\"%s\" value=\"\"><br>", PersonLogic.BIRTH);
            out.println("<br>");

            out.println("</div>");

            out.println("<div style=\"margin-top:50px;background-color: lightblue;padding:20px;border-radius:15px; postion:relative; \">");

            out.println("Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.NAME);
            out.println("Administrator:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.ADMINISTRATOR);
            out.println("<br>");
            out.println("Hospital:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.HOSPITAL);
            out.println("<br>");
            out.println("Tested:<br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"1\"><br>", DonationRecordLogic.TESTED);
            out.printf("<label for=\"tested\">Positive</label><br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"0\"><br>", DonationRecordLogic.TESTED);
            out.printf("<label for=\"tested\">Negative</label><br>");
            out.println("<br>");
            out.println("Created:<br>");
            out.printf("<input type=\"datetime-local\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.CREATED);
            out.println("<br>");
            out.println("</div>");

            out.println("<input style=\" position:absolute;left:700px;top:1050px;width: 200px; \" type=\"submit\" name=\"add\" value=\"Add\">");

            out.println("</form>");

            if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            log("POST");
            BloodDonationLogic bDLogic = LogicFactory.getFor("BloodDonation");
            BloodBankLogic bBLogic = LogicFactory.getFor("BloodBank");
            PersonLogic pLogic = LogicFactory.getFor("Person");
            DonationRecordLogic dLogic = LogicFactory.getFor("DonationRecord");

            try {
                BloodDonation bD = bDLogic.createEntity(request.getParameterMap());

                Person person = pLogic.createEntity(request.getParameterMap());

                DonationRecord donationRecord = dLogic.createEntity(request.getParameterMap());

                if (request.getParameter(BloodDonationLogic.BANKID).isEmpty()) {
                    bD.setBloodBank(null);
                } else {
                    BloodBank bb = bBLogic.getWithId(Integer.parseInt(request.getParameter(BloodDonationLogic.BANKID)));
                    if (bb == null) {
                        throw new ValidationException("Foreign Constraint Fails, ID = " + request.getParameter(BloodDonationLogic.BANKID)
                                + " Does not exists in BloodBank Database");
                    }
                    bD.setBloodBank(bb);
                }
                pLogic.add(person);
                dLogic.add(donationRecord);
                bDLogic.add(bD);
            } catch (NumberFormatException ex) {
                errorMessage = ex.getMessage();
            }
            if (request.getParameter("add") != null) {
                //if add button is pressed return the same page
                processRequest(request, response);
            }
        } catch (IOException | ServletException ex) {
            Logger.getLogger(CreateBloodDonation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(" , ")
                .append("Value/s= ").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Create a BloodDonation Entity";
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