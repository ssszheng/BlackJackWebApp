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
@WebServlet(name = "possiblemovesServlet", urlPatterns = {"/possiblemoves"})
public class possiblemovesServlet extends HttpServlet {

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
        response.setContentType("text/plain");
        try (PrintWriter out = response.getWriter()) {
            /* Deal one card to the user and remove it from deck . */
            HttpSession session = request.getSession(false);

          
            if (session == null || !request.isRequestedSessionIdValid()) {
                //comes here when session is invalid. 
                response.sendError(HttpServletResponse.SC_NOT_FOUND, " no active game ");
                return;
            } else {
                Game g = (Game) session.getAttribute(session.getId());
                String possibleMoves = "No advice yet";
                List<Card> userCards = g.getUserCards();
                Integer userScore = g.scoreWithoutACE(userCards);

                boolean userACE = g.ACEchecker(userCards);

                if (userACE == true) {
                    if (userScore + 11 <= 21) {
                        userScore += 11;
                    } else {
                        userScore += 1;
                    }
                }
                if (userScore > 21) {
                    possibleMoves = "Stand. (cannot hit when the hand is greater than 21.)";
                } else if (17 <= userScore && userScore <= 21) {
                    possibleMoves = "Hit or Stand. (Stand advised as score >= 17)";
                } else if (17 > userScore) {
                    possibleMoves = "Hit or Stand. (Hit advised as score < 17)";
                }

                out.println("Possible user moves available: " + possibleMoves);

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
