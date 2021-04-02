package view;

import common.ValidationException;
import entity.BloodBank;
import entity.BloodDonation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
@WebServlet(name = "BloodDonationTableJSP", urlPatterns = {"/BloodDonationTableJSP"})
public class BloodDonationTableJSP extends HttpServlet {

    private void fillTableData(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.setAttribute("entities", extractTableData(req));
        req.setAttribute("request", toStringMap(req.getParameterMap()));
        req.setAttribute("path", path);
        req.setAttribute("title", path.substring(1));
        req.getRequestDispatcher("/jsp/ShowTable-Blood_Donation.jsp").forward(req, resp);
    }

    private List<?> extractTableData(HttpServletRequest req) {
        try {
            String search = req.getParameter("searchText");
            BloodDonationLogic logic = LogicFactory.getFor("BloodDonation");
            req.setAttribute("columnName", logic.getColumnNames());
            req.setAttribute("columnCode", logic.getColumnCodes());
            List<BloodDonation> list;
            if (search != null) {
                list = logic.search(search);
            } else {
                list = logic.getAll();
            }
            if (list == null || list.isEmpty()) {
                return Collections.emptyList();
            }
            return appendDataToNewList(list, logic::extractDataAsList);
        } catch (Exception ex) {
            Logger.getLogger(BloodDonationTableJSP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private <T> List<?> appendDataToNewList(List<T> list, Function<T, List<?>> toArray) {
        List<List<?>> newlist = new ArrayList<>(list.size());
        list.forEach(i -> newlist.add(toArray.apply(i)));
        return newlist;
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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            log("POST");
            BloodDonationLogic logic = LogicFactory.getFor("BloodDonation");
            BloodBankLogic bBLogic = LogicFactory.getFor("BloodBank");

            if (req.getParameter("edit") != null) {
                BloodDonation bd = logic.updateEntity(req.getParameterMap());
                BloodBank bb = bBLogic.getWithId(Integer.parseInt(req.getParameter(BloodDonationLogic.BANKID)));
                if (bb == null) {
                    throw new ValidationException("Foreign Constraint Fails");
                }
                bd.setBloodBank(bb);
                logic.update(bd);
            } else if (req.getParameter("delete") != null) {
                String[] ids = req.getParameterMap().get("deleteMark");
                for (String id : ids) {
                    logic.delete(logic.getWithId(Integer.valueOf(id)));
                }
            }
            fillTableData(req, resp);
        } catch (Exception ex) {
            PrintWriter out = resp.getWriter();
            out.println("<h1 style=\"margin-left:200px;margin-top:300px;color:red\" >Foreign Key Constraint Fails</h1>");
            Logger.getLogger(BloodDonationTableJSP.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
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