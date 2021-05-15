/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_20010847.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "wonServlet", urlPatterns = {"/won"})
public class wonServlet extends HttpServlet {

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
                String whoseTurn = (String) g.getWhoseTurn();

                if (whoseTurn.equals("Computer")) {

                    List<Card> userCards = g.getUserCards();
                    List<Card> aiCards = g.getAICards();
                    Integer aiScore = g.scoreWithoutACE(aiCards);
                    Integer userScore = g.scoreWithoutACE(userCards);

                    boolean aiACE = g.ACEchecker(aiCards);
                    boolean userACE = g.ACEchecker(userCards);

                    if (aiACE == true) {
                        if (aiScore + 11 <= 21) {
                            aiScore += 11;
                        } else {
                            aiScore += 1;
                        }
                    }
                    if (userACE == true) {
                        if (userScore + 11 <= 21) {
                            userScore += 11;
                        } else {
                            userScore += 1;
                        }
                    }

                    if (aiScore <= 21 && userScore <= 21) {
                        if (aiScore > userScore) {
                            g.setWinner("Computer");
                        } else if (aiScore == userScore) {
                            g.setWinner("Draw");
                        } else {
                            g.setWinner("User");
                        }
                    } else if (aiScore > 21 && userScore <= 21) {
                        g.setWinner("User");
                    } else if (aiScore <= 21 && userScore > 21) {
                        g.setWinner("Computer");
                    } else {
                        g.setWinner("Draw");
                    }

                    String winner = g.getWinner();
                    out.println("Winner: " + winner);

                    JSONObject jsonObject = new JSONObject();
                    //Inserting key-value pairs into the json object
                    jsonObject.put("GameSession", jsessionid);
                    jsonObject.put("Winner", winner);

                    //read json file
                    File file = new File("stats.json");
                    if (file.exists() == false) {
                        file.createNewFile();
                    }

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    StringBuilder st = new StringBuilder();
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        st.append(line);
                    }

                    //add new value to Jsonarray
                    if (st.length() == 0) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        //save array to file    
                        FileWriter fw = new FileWriter("stats.json");
                        fw.write(jsonArray.toString());
                        fw.flush();
                        fw.close();
                    } else {
                        JSONArray jsonArray = new JSONArray(st.toString());

                        jsonArray.put(jsonObject);
                        //save array to file    
                        FileWriter fw = new FileWriter("stats.json");
                        fw.write(jsonArray.toString());
                        fw.flush();
                        fw.close();
                    }

                    out.println("JSON file updated: " + jsonObject.toString());
                    g.setStatus("not start");
                    // Invalidate the session and removes any attribute related to it
                    session.invalidate();

                } else {

                    getServletContext().getRequestDispatcher("/blackjack.jsp" + sessionIdUrl).forward(request, response);

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
