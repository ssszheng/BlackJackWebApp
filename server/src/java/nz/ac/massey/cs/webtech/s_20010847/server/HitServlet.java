/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_20010847.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author szheng
 */
@WebServlet(name = "HitServlet", urlPatterns = {"/move/hit"})
public class HitServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {

            /* Deal one card to the user and remove it from deck . */
            String url = request.getRequestURL().toString();
            String jsessionid = url.split("jsessionid=")[1];
            HttpSession session = request.getSession(false);
            if (session == null || !request.isRequestedSessionIdValid()) {
                //comes here when session is invalid. 
                response.sendError(HttpServletResponse.SC_NOT_FOUND, " no active game ");
                return;
            } else {
                Game g = (Game) session.getAttribute(jsessionid);
                String sessionIdUrl = ";jsessionid=" + jsessionid;
                String whoseTurn = (String) g.getWhoseTurn();

                // 400 SC_BAD_REQUEST, if  not the user’s turn or user’s hand is already bust (user hand greater than 21) 
                //404 Not Found If there is not active game 
                if (g.getStatus() == "not start") {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, " no active game ");
                    return;
                } else {

                    if (whoseTurn.equals("Computer")) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, " Not your turn ");
                        return;
                    } else if (whoseTurn.equals("User")) {
                        List<Card> cards = g.getCards();
                        List<Card> userCards = g.getUserCards();
                        Integer userScore = g.scoreWithoutACE(userCards);
                        boolean userACE = g.ACEchecker(userCards);

                        if (userACE == true) {
                            if (userScore + 1 <= 21) {
                                g.adduserCards(cards.get(0));
                                g.removeCard(cards.get(0));
                            } else {

                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, " Can't hit score > 21");
                                return;
                            }
                        } else {
                            if (userScore > 21) {

                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Can't hit score > 21");
                                return;
                            } else {
                                g.adduserCards(cards.get(0));
                                g.removeCard(cards.get(0));
                            }
                        }

                        g.setWhoseTurn("Computer");
                        getServletContext().getRequestDispatcher("/move/stand" + sessionIdUrl).forward(request, response);
                    }

                }
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
