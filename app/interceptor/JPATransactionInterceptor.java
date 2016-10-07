package interceptor;

import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import play.db.jpa.JPAApi;


public class JPATransactionInterceptor implements MethodInterceptor {
    private Provider<JPAApi> jpaApiProvider;

    public JPATransactionInterceptor(Provider<JPAApi> api) {
        this.jpaApiProvider = api;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        JPAApi jpa = jpaApiProvider.get();
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
