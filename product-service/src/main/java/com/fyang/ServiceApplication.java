package com.fyang;

import com.fyang.bean.Product;
import com.fyang.service.IProductService;
import com.fyang.service.ProductService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceApplication {

    public static void main(String[] args) {
        try {
            System.out.println("服务端启动：：");
            ServerSocket serverSocket = new ServerSocket(8085);
            while (true){
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


                String apiClassName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class[] parameterTypes = (Class[]) objectInputStream.readObject();
                Object[] orgMethods = (Object[]) objectInputStream.readObject();

                System.out.println("read apiClassName is :::"+ apiClassName);
                System.out.println("read methodName is :::"+ methodName);
                System.out.println("read parameterTypes is :::"+ parameterTypes);
                System.out.println("read orgMethods is :::"+ orgMethods);


                Class clazz = null;
                if(apiClassName.equals(IProductService.class.getName())){
                    clazz = ProductService.class;
                }
                Method method = clazz.getMethod(methodName, parameterTypes);
                Object invoke = method.invoke(clazz.newInstance(),orgMethods);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(invoke);
                objectOutputStream.flush();

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
