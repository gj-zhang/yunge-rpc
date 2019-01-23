package com.rancho.yunge.provider;

import com.rancho.yunge.context.ContextHolder;
import com.rancho.yunge.params.RpcRequestWrapper;
import com.rancho.yunge.params.RpcResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DefaultRpcProvider extends AbstractRpcProvider {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRpcProvider.class);

    public DefaultRpcProvider(ContextHolder contextHolder) {
        super(contextHolder);
    }

    @Override
    public RpcResponseWrapper invokeService(RpcRequestWrapper requestWrapper) {
        RpcResponseWrapper response = new RpcResponseWrapper();

        response.setRequestId(requestWrapper.getRequestId());
        String serviceKey = contextHolder.makeServiceKey(requestWrapper.getClassName(), requestWrapper.getVersion());
        Object service = contextHolder.getService(serviceKey);
        if (service == null) {
            response.setErrorMsg("can not found service " + requestWrapper.getClassName() + ", version " + requestWrapper.getVersion());
            return response;
        }

        if (System.currentTimeMillis() - requestWrapper.getCreateTime() > 3 * 60 * 1000 ) {
            response.setErrorMsg("the time between create and execute greater than limits 3 * 60 * 1000");
        }

        try {
            Class<?> serviceClass = service.getClass();
            String methodName = requestWrapper.getMethodName();
            Class<?>[] parameterTypes = requestWrapper.getParameterTypes();
            Object[] parameters = requestWrapper.getParameters();
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            final Object result = method.invoke(service, parameters);
            response.setResult(result);
        } catch (NoSuchMethodException e) {
            logger.error("can not find method named " + requestWrapper.getMethodName() + " in class " + serviceKey);
            response.setErrorMsg(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("can not access method named " + requestWrapper.getMethodName() + " in class " + serviceKey);
            response.setErrorMsg(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("error occurred when invoke method named " + requestWrapper.getMethodName() + " in class " + serviceKey);
            response.setErrorMsg(e.getMessage());
        } catch (Throwable e) {
            logger.error("throwable error occurred when invoke method named " + requestWrapper.getMethodName() + " in class " + serviceKey);
            response.setErrorMsg(e.getMessage());
        }
        return response;
    }

}
