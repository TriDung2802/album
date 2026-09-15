package murach.download;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import murach.business.User;
import murach.data.UserIO;
import murach.util.CookieUtil;

@WebServlet("/download")

public class DownloadServlet extends HttpServlet{
   @Override
   public void doGet(HttpServletRequest request,
           HttpServletResponse response)
           throws IOException,ServletException{
       
       //get current action
       String action=request.getParameter("action");
       if(action==null){
           action="viewAlbums";
       }
       //perform action and set URL to appropriae page
       String url="/index.jsp";
       if(action.equals("viewAlbums")){
           url="/index.jsp";
       }
       else if(action.equals("checkUser")){
           url=checkUser(request,response);
       }
       
       //forward to view
       getServletContext()
               .getRequestDispatcher(url)
               .forward(request,response);
   }
   @Override
   public void doPost(HttpServletRequest request,
           HttpServletResponse response)
           throws IOException, ServletException{
       String action=request.getParameter("action");
       
       String url ="/index.jsp";
       if(action.equals("registerUser")){
           url=registerUser(request,response);
       }
       
       getServletContext()
               .getRequestDispatcher(url)
               .forward(request,response);
   }
   private String checkUser(HttpServletRequest request,
           HttpServletResponse response){
       String productCode=request.getParameter("productCode");
       HttpSession session=request.getSession();
       session.setAttribute("productCode",productCode);
       User user=(User) session.getAttribute("user");
       String url;
       if(user==null){
           Cookie[] cookies = request.getCookies();
           String emailAddress = CookieUtil.getCookieValue(cookies,"emailCookie");
           
           if (emailAddress==null || emailAddress.equals("")){
               url="/register.jsp";
           }
           else {
                ServletContext sc = getServletContext();
                String path = sc.getRealPath("/WEB-INF/EmailList.txt");
                user = UserIO.getUser(emailAddress, path);
                session.setAttribute("user", user);
                url = "/" + productCode + "_download.jsp";
            }
       }
       else {
            url = "/" + productCode + "_download.jsp";
        }
        return url;
   }
   private String registerUser(HttpServletRequest request,
            HttpServletResponse response) {

        // get the user data
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        // store the data in a User object
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        ServletContext sc = getServletContext();
        String path = sc.getRealPath("/WEB-INF/EmailList.txt");
        UserIO.add(user, path);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        Cookie c = new Cookie("emailCookie", email);
        c.setMaxAge(60 * 60 * 24 * 365 * 2); 
        c.setPath("/");            
        response.addCookie(c);

        String productCode = (String) 
            session.getAttribute("productCode");
        String url = "/" + productCode + "_download.jsp";
        return url;
    }
}