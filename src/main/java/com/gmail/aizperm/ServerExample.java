package com.gmail.aizperm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServerExample {
    public static void main(String[] args) throws Exception {

        JsonObject json = (JsonObject) new JsonParser().parse("{\"server\":840736,\"photo\":\"[{\\\"photo\\\":\\\"c14368251b:z\\\",\\\"sizes\\\":[[\\\"s\\\",\\\"840736868\\\",\\\"4cfea\\\",\\\"1tjpWaPvu1c\\\",75,75],[\\\"m\\\",\\\"840736868\\\",\\\"4cfeb\\\",\\\"BPGL0dkRK94\\\",130,130],[\\\"x\\\",\\\"840736868\\\",\\\"4cfec\\\",\\\"JoO_jmEjhNI\\\",604,604],[\\\"y\\\",\\\"840736868\\\",\\\"4cfed\\\",\\\"tyO-y22tzF8\\\",807,807],[\\\"z\\\",\\\"840736868\\\",\\\"4cfee\\\",\\\"N1X2vZHIfbw\\\",960,960],[\\\"o\\\",\\\"840736868\\\",\\\"4cfef\\\",\\\"_e03vFmF-ng\\\",130,130],[\\\"p\\\",\\\"840736868\\\",\\\"4cff0\\\",\\\"hX4hpfu43t8\\\",200,200],[\\\"q\\\",\\\"840736868\\\",\\\"4cff1\\\",\\\"a0B5keHX2qg\\\",320,320],[\\\"r\\\",\\\"840736868\\\",\\\"4cff2\\\",\\\"vglZlQMj2TM\\\",510,510]],\\\"kid\\\":\\\"88fbf8120da97e2561db1c8685227a48\\\",\\\"debug\\\":\\\"xszmzxzyzzzozpzqzrz\\\"}]\",\"hash\":\"b02139fcb7454f23141801ec5e5900b0\"}");
        JsonPrimitive photo = json.getAsJsonPrimitive("photo");
        String hash = json.getAsJsonPrimitive("hash").getAsString();
        json.getAsJsonPrimitive("server").getAsInt();
        String str = photo.getAsString();
        System.out.println(json);

        Server server = new Server(8888);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

                if ("/auth".equalsIgnoreCase(target)) {
                    String code = request.getParameter("code");
                    if (code == null) {
                        response.getOutputStream().println("<html><body>Fail code</body></html>");
                        response.getOutputStream().close();
                        return;
                    }
                    response.getOutputStream().println("<html><body>Ok. Code = " + code + "</body></html>");
                    response.getOutputStream().close();
                    return;
                }
                response.getOutputStream().println("<html><body>Unsupported method " + target + "</body></html>");
                response.getOutputStream().close();
            }
        });
        server.start();
        server.join();
    }
}
