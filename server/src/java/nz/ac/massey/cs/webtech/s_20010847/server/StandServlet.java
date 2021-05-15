/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_20010847.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
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
@WebServlet(name = "StandServlet", urlPatterns = {"/move/stand"})
public class StandServlet extends HttpServlet {

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
            String url = request.getRequestURL().toString();
            String jsessionid = url.split("jsessionid=")[1];
            HttpSession session = request.getSession(false);
            //404 Not Found If there is not active game 
            if (session == null || !request.isRequestedSessionIdValid()) {
                //comes here when session is invalid. 
                response.sendError(HttpServletResponse.SC_NOT_FOUND, " no active game ");
                return;
            } else {
                Game g = (Game) session.getAttribute(jsessionid);
                String sessionIdUrl = ";jsessionid=" + jsessionid;
                String whoseTurn = g.getWhoseTurn();

                if (whoseTurn.equals("User")) {
                    g.setWhoseTurn("Computer");
                }

                if (whoseTurn.equals("Computer")) {

                    List<Card> cards = g.getCards();

                    //the predetermined rule: Add new card to the computer’s hand value as long as the value of the computer’s hand is 17 or below. 
                    List<Card> userCards = g.getUserCards();
                    List<Card> aiCards = g.getAICards();
                    Integer aiScore = g.scoreWithoutACE(aiCards);
                    Integer userScore = g.scoreWithoutACE(userCards);
                    //decise to make ACE 1 or 11
                    for (Card c : aiCards) {
                        if (c.value == Value.ACE) {
                            if (aiScore + 11 <= 21) {
                                aiScore += 11;
                            } else if (aiScore + 1 <= 21) {
                                aiScore += 1;
                            } else if (aiScore + 1 > 21) {
                                aiScore += 1;
                            }
                        }
                    }
                    if (aiScore <= 17) {
                        g.addAICards(cards.get(0));
                        g.removeCard(cards.get(0));
                    } else {
                        getServletContext().getRequestDispatcher("/won" + sessionIdUrl).forward(request, response);
                        return;
                    }

                    g.setWhoseTurn("User");
                }

                getServletContext().getRequestDispatcher("/won" + sessionIdUrl).forward(request, response);
                return;

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
