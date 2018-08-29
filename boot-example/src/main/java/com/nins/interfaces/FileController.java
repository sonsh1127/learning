package com.nins.interfaces;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @GetMapping("/download/large")
    public void downloadLargeFile(HttpServletResponse response) throws Exception {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"test.txt\"");
        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("/Users/kakao/test.txt")));
        int nRead;

        while ((nRead = inputStream.read()) != -1) {
            response.getWriter().write(nRead);
        }

    }

}
