/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.*;
import Dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author DANA
 */
public class StudentListPage extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StudentListPage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentListPage at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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

        try {
            int examId = Integer.parseInt(request.getParameter("examId"));
            HttpSession session = request.getSession();
            String tracker = (String) session.getAttribute("tracker");
            Connection conn = (Connection) session.getAttribute("conn");
            StudentExamDao studentExamDao = new StudentExamDao();
            ArrayList<Student> students = (ArrayList<Student>) studentExamDao.findEnteredStudents(conn, examId);
            try {
                if (tracker.equals("teacher")) {
                    request.setAttribute("studentsEntered", students);
                    RequestDispatcher rd = request.getRequestDispatcher("StudentListPage.jsp");
                    rd.forward(request, response);
                } else {
                    response.sendRedirect("login.jsp");
                }
            } catch (Exception e) {
                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            response.sendRedirect("login.jsp");
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
        try {
            String[] studentIdsString = request.getParameterValues("studentsIds");
            String batch = request.getParameter("batch");
            System.out.println("HERE!!!");
            HttpSession session = request.getSession();
            Connection conn = (Connection) session.getAttribute("conn");
            Student student = (Student) session.getAttribute("student");
            Exam exam = (Exam) session.getAttribute("exam");
            StudentExamDao studentExamDao = new StudentExamDao();
            for (int i = 0; i < studentIdsString.length; ++i) {
                System.out.println(studentIdsString[i]);
                studentExamDao.setAllowedResetEntered(conn, Integer.parseInt(studentIdsString[i]), exam.getExamId(), batch);
            }
            response.sendRedirect("QuestionPage?examId=" + exam.getExamId());
        } catch (Exception e) {
            response.sendRedirect("login.jsp");
        }

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
