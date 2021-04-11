package view;

import common.ValidationException;
import entity.BloodBank;
import entity.BloodDonation;
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
import logic.BloodDonationLogic;
import logic.LogicFactory;

/**
 *
 * @author Ehsan
 */
@WebServlet(name = "BloodDonationTableInsertJSP", urlPatterns = {"/CreateBloodDonationJSP"})

public class CreateBloodDonationJSP extends HttpServlet {

    private void fillTableData(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.setAttribute("request", toStringMap(req.getParameterMap()));
        req.setAttribute("path", path);
        req.setAttribute("title", path.substring(1));
        req.getRequestDispatcher("/jsp/ShowCreate-Blood_Donation.jsp").forward(req, resp);

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            log("POST");
            BloodDonationLogic bDLogic = LogicFactory.getFor("BloodDonation");
            BloodBankLogic bBLogic = LogicFactory.getFor("BloodBank");

            try {
                BloodDonation bD = bDLogic.createEntity(request.getParameterMap());

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

                bDLogic.add(bD);
            } catch (NumberFormatException ex) {

            }
            if (request.getParameter("add") != null) {
                //fillTableData(request, response);
                PrintWriter out = response.getWriter();
                out.print("Values added :");
                out.println(toStringMap(request.getParameterMap()));

            } else if (request.getParameter("view") != null) {
                //if view button is pressed redirect to the appropriate table
                response.sendRedirect("BloodDonationTable");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("GET");
        fillTableData(req, resp);

    }

    /**
     * Handles the HTTP <code>PUT</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("PUT");
        doPost(req, resp);
    }

    /**
     * Handles the HTTP <code>DELETE</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("DELETE");
        doPost(req, resp);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Smaple of Account Table using JSP";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
