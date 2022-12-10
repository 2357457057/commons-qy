package top.yqingyu.common.nio$server.event$http.web.controller;

import top.yqingyu.common.nio$server.event$http.annotation.QyController;
import top.yqingyu.common.nio$server.event$http.compoment.HttpMethod;
import top.yqingyu.common.nio$server.event$http.compoment.MultipartFile;

import java.io.IOException;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.nio$server.event$http.web.controller.UploadDemo
 * @description
 * @createTime 2022年10月27日 16:58:00
 */
@QyController(path = "upload")
public class UploadDemo {


    @QyController(path = "page", method = {HttpMethod.GET})
    public String page(String name) {
        return "  <form action=\"/upload/upper\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                "    <input type=\"file\" name=\"uploadFile\" value=\"请选择文件\">\n" +
                "    <input type=\"submit\" value=\"upper1\">\n" +
                "  </form>";
    }

    @QyController(path = "upper", method = {HttpMethod.GET, HttpMethod.POST})
    public void upper(MultipartFile file) throws IOException {
        System.out.println(file.getFileName());
        file.saveAs("I:/" + file.getFileName());
        System.out.println(file);
    }
}
