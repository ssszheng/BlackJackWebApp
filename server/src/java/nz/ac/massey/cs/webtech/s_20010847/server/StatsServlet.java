package nz.ac.massey.cs.webtech.s_20010847.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author szheng
 */
@WebServlet(name = "StatsServlet", urlPatterns = {"/stats"})

public class StatsServlet extends HttpServlet {

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
        //number of games played, and user win percentage.
            response.setContentType("text/plain;charset=UTF-8"); 
               
            File file = new File("stats.json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder st = new StringBuilder();
            String line;
            while( (line = br.readLine()) != null) {
               st.append(line);
            }
            
            int userWon = 0;

            JSONArray jsonArray = new JSONArray(st.toString());
            JSONObject jsonObject ;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String gameSession = (String) jsonObject.get("GameSession");
                out.print("GameSession: "+gameSession);

                String winner= (String)jsonObject.get("Winner");
                out.println(", Winner: "+winner);

                if ("User".equals(winner)){
                    userWon+=1;
                }
                }
                    
                int numberOfGames = jsonArray.length();
                double winPercentage =  (double) userWon / numberOfGames *100;
                out.println("Number of games played: "+ numberOfGames);
                out.println("Number of games user won: "+ userWon);
                out.printf("User win percentage: %.2f %%", winPercentage);
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
       
    
            processRequest(request,response);
            
            
            
            
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
