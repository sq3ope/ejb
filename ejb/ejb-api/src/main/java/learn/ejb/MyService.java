package learn.ejb;

import javax.ejb.Remote;

@Remote
public interface MyService {
    String sayHello();
}
