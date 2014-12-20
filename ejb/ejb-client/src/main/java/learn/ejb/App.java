package learn.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class App {
    public static void main(String[] args) throws NamingException {
        String lookupName = getMyServiceLookupName();

        Context ctx = new InitialContext();
        MyService myService = (MyService) ctx.lookup(lookupName);

        System.out.println(String.format("myService.sayHello() reponse: '%s'", myService.sayHello()));
    }

    private static String getMyServiceLookupName() {
        /* The app name is the EAR name of the deployed EJB without .ear suffix. */
        String appName = "ear-1.0-SNAPSHOT";
        /* The module name is the JAR name of the deployed EJB without the .jar suffix */
        String moduleName = "ejb-1.0-SNAPSHOT";
        /*
         * AS7 allows each deployment to have an (optional) distinct name. This
         * can be an empty string if distinct name is not specified.
         */
        String distinctName = "";
        /* The EJB bean implementation class name */
        String beanName = "MyServiceBean";
        /* Fully qualified remote interface name */
        String interfaceName = "learn.ejb.MyService";

        return "ejb:" + appName + "/" + moduleName + "/" + distinctName
                + "/" + beanName + "!" + interfaceName;
    }
}
