package interceptor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import play.db.jpa.JPAApi;


public class JPATransactionInterceptor implements MethodInterceptor {
    @Inject private JPAApi jpa;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return jpa.withTransaction(() -> {

            System.out.println("em call:" + jpa.em().hashCode());
            try {
                return invocation.proceed();
            } catch (Throwable e) {
               throw new RuntimeException(e);
            }
        });
    }
}
