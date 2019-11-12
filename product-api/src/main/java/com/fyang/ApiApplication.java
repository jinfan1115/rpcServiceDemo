package com.fyang;

import com.fyang.bean.Product;
import com.fyang.service.IProductService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ApiApplication {

    public static void main(String[] args) {
        System.out.println("客户端发起方法调用：：");
        IProductService productService = (IProductService)rpc(IProductService.class);
        Product product = productService.queryById(100);
        System.out.println(product);
    }

    private static Object rpc(final Class clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket("127.0.0.1",8085);

                        String apiClassName = clazz.getName();
                        String methodName = method.getName();
                        Class[] parameterTypes = method.getParameterTypes();

                        System.out.println("write apiClassName is :::"+ apiClassName);
                        System.out.println("write methodName is :::"+ methodName);
                        System.out.println("write parameterTypes is :::"+ parameterTypes);
                        System.out.println("write orgMethods is :::"+ args);

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeUTF(apiClassName);
                        objectOutputStream.writeUTF(methodName);
                        objectOutputStream.writeObject(parameterTypes);
                        objectOutputStream.writeObject(args);
                        objectOutputStream.flush();

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        Object o = objectInputStream.readObject();
                        objectInputStream.close();
                        objectOutputStream.close();

                        socket.close();
                        return o;

                    }
                });
    }
}
