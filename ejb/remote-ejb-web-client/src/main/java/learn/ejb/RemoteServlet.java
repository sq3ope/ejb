package learn.ejb;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RemoteServlet extends HttpServlet {
    @EJB(lookup = "ejb:MyApplication/MyModule/StatelessServiceBean!learn.ejb.StatelessServiceRemote")
    StatelessService statelessService;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        out.println("Reponse from stateless bean: " + statelessService.printAccountDetails());
        out.flush();
        out.close();

    }
}
