package nz.ac.massey.cs.webtech.s_20010847.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@WebServlet(name = "StartServlet", urlPatterns = {"/start"})

public class StartServlet extends HttpServlet {

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
              
        Game g = new Game();
        g.setStatus("started");
        

        g.createDeck();

        List<Card> cards = g.shuffleCards();
        //deal two cards to user and computer repsectively
        g.adduserCards(cards.get(0));
        g.removeCard(cards.get(0));
        g.adduserCards(cards.get(0));
        g.removeCard(cards.get(0));

        g.addAICards(cards.get(0));
        g.removeCard(cards.get(0));
        g.addAICards(cards.get(0));
        g.removeCard(cards.get(0));


        HttpSession session = request.getSession();
        String id = session.getId();
        String sessionIdUrl = ";jsessionid="+id;
        session.setAttribute(id, g);
        getServletContext().getRequestDispatcher("/blackjack.jsp"+ sessionIdUrl).forward(request, response);
            
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

  
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
