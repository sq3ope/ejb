package learn.ejb;

import javax.ejb.Stateless;

@Stateless
public class MyServiceBean implements MyService {
    @Override
    public String sayHello() {
        return "Hello";
    }
}
