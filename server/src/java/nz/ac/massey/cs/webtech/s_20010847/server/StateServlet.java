package nz.ac.massey.cs.webtech.s_20010847.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author szheng
 */
@WebServlet(name = "StateServlet", urlPatterns = {"/state"})

public class StateServlet extends HttpServlet {

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

        try (PrintWriter out = response.getWriter()) {
            //Game state (player hands, whoseTurn, winner, etc)
            response.setContentType("application/json;charset=UTF-8");
            HttpSession session = request.getSession(false);
           //404 Not Found If there is not active game 
            if (session == null || !request.isRequestedSessionIdValid()) {
                //comes here when session is invalid. 
                response.sendError(HttpServletResponse.SC_NOT_FOUND, " no active game ");
                return;
            } else {
            Game g = (Game) session.getAttribute(session.getId());
           
                String whoseTurn = (String) g.getWhoseTurn();
                String winner = g.getWinner();
                List<Card> aiCards = g.getAICards();
                List<Card> userCards = g.getUserCards();
                JSONArray userList = new JSONArray();
                Iterator<Card> iterUser = userCards.iterator();
                while (iterUser.hasNext()) {
                    Card c = iterUser.next();
                    Object cs = c.suite;
                    Object cv = c.value;
                    userList.put(c);
                }

                JSONArray aiList = new JSONArray();
                Iterator<Card> iterAI = aiCards.iterator();
                while (iterAI.hasNext()) {
                    Card a = iterAI.next();
                    Object as = a.suite;
                    Object av = a.value;
                    aiList.put(a);
                }

                JSONObject jObj = new JSONObject();
                jObj.put("ComputerHand", aiList);
                jObj.put("UserHand", userList);
                jObj.put("Winner", winner);
                jObj.put("WhoseTurn", whoseTurn);
                jObj.put("GameStatus", g.getStatus());

                out.println(jObj.toString());

            }
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

        String url = request.getRequestURL().toString();
        if (!url.contains("jsessionid")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, " no active game ");
        } else {
            processRequest(request, response);
        }

        // RequestDispatcher dispatcher = request.getRequestDispatcher("blackjack.jsp");
        //dispatcher.forward(request, response);
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
