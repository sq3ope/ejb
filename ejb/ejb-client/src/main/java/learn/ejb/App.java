package learn.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class App {
    /* The app name is the EAR name of the deployed EJB without .ear suffix. */
    private static String APPNAME = "ear-1.0-SNAPSHOT";
    /* The module name is the JAR name of the deployed EJB without the .jar suffix */
    private static String MODULENAME = "ejb-1.0-SNAPSHOT";
    /*
     * AS7 allows each deployment to have an (optional) distinct name. This
     * can be an empty string if distinct name is not specified.
     */
    private static String DISTINCTNAME = "";

    public static void main(String[] args) throws NamingException {
        new App().run();
    }

    public void run() throws NamingException {
        Context ctx = new InitialContext();

        System.out.println("\n--------- Testing stateless service ---------\n");

        final StatelessService statelessService = (StatelessService) ctx.lookup(getStatelessServiceLookupName());

        int numOfThreads = 100;
        final CyclicBarrier initialBarrier = new CyclicBarrier(numOfThreads);
        final CyclicBarrier finalBarrier = new CyclicBarrier(numOfThreads);

        for (int i = 0; i < numOfThreads; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        initialBarrier.await();
                        // all threads will invoke this request simultaneously
                        // some threads will hit the same bean in the pool, but some will not
                        // stateless been does not guarantee conversational consistency
                        System.out.println("*** bean request #" + finalI + " " + statelessService.printAccountDetails());
                        finalBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }, "T" + i).start();
        }

        System.out.println("\n--------- Testing stateful service ---------\n");

        for (int i = 0; i < numOfThreads; i++) {
            System.out.println("*** bean instance #" + i);
            StatefulService statefulService = (StatefulService) ctx.lookup(getStatefulServiceLookupName());

            // first request
            System.out.println(statefulService.printAccountDetails());

            double deposit = 10.0;
            System.out.println(String.format("depositing %s", deposit));
            statefulService.deposit(deposit);

            // second request is guaranteed to refer to the same instance that the first one
            System.out.println(statefulService.printAccountDetails());
        }
    }

    private String getStatelessServiceLookupName() {
        /* The EJB bean implementation class name */
        String beanName = "StatelessServiceBean";
        /* Fully qualified remote interface name */
        String interfaceName = "learn.ejb.StatelessService";

        return "ejb:" + APPNAME + "/" + MODULENAME + "/" + DISTINCTNAME
                + "/" + beanName + "!" + interfaceName;
    }

    private String getStatefulServiceLookupName() {
        /* The EJB bean implementation class name */
        String beanName = "StatefulServiceBean";
        /* Fully qualified remote interface name */
        String interfaceName = "learn.ejb.StatefulService";

        return "ejb:" + APPNAME + "/" + MODULENAME + "/" + DISTINCTNAME
                + "/" + beanName + "!" + interfaceName + "?stateful";
    }
}
