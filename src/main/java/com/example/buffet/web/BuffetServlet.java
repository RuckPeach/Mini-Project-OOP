package com.example.buffet.web;

import com.example.buffet.service.BuffetManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BuffetServlet", urlPatterns = {"/buffet"})
public class BuffetServlet extends HttpServlet {

    private BuffetManager manager;

    @Override
    public void init() throws ServletException {
        super.init();
        manager = BuffetManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // This method is used to display the main page
        request.setAttribute("manager", manager);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "addQueue":
                    addQueue(request);
                    break;
                case "assignTable":
                    assignTable(request);
                    break;
                case "cancelQueue":
                    cancelQueue(request);
                    break;
                case "freeTable":
                    freeTable(request);
                    break;
                case "finishCleaning":
                    finishCleaning(request);
                    break;
            }
        }

        // Redirect back to the main page to show the updated state
        response.sendRedirect(request.getContextPath() + "/buffet");
    }

    private void addQueue(HttpServletRequest request) {
        String name = request.getParameter("customerName");
        String phone = request.getParameter("customerPhone");
        try {
            int count = Integer.parseInt(request.getParameter("customerCount"));
            if (name != null && !name.trim().isEmpty() && count > 0) {
                manager.addQueue(name, phone, count);
            }
        } catch (NumberFormatException e) {
            // Handle error - maybe set an attribute to show an error message
        }
    }

    private void assignTable(HttpServletRequest request) {
        String queueId = request.getParameter("queueId");
        if (queueId != null) {
            manager.assignTable(queueId);
        }
    }

    private void cancelQueue(HttpServletRequest request) {
        String queueId = request.getParameter("queueId");
        if (queueId != null) {
            manager.cancelQueue(queueId);
        }
    }

    private void freeTable(HttpServletRequest request) {
        try {
            int tableId = Integer.parseInt(request.getParameter("tableId"));
            manager.freeTable(tableId);
        } catch (NumberFormatException e) {
            // Handle error
        }
    }
    
    private void finishCleaning(HttpServletRequest request) {
        try {
            int tableId = Integer.parseInt(request.getParameter("tableId"));
            manager.finishCleaning(tableId);
        } catch (NumberFormatException e) {
            // Handle error
        }
    }
}
