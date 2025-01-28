package br.com.jamesalves.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.jamesalves.demo.services.DemoService;
import net.sf.jasperreports.engine.JRException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloDemoController {
    private final DemoService domoService;

    public HelloDemoController(DemoService domoService) {
        this.domoService = domoService;
    }

    @GetMapping("/")
    public String getMethodName() {
        try {
            domoService.generateUserReport("report-teste");
            domoService.generateReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> donwloadReport() {
        try {
            byte[] report = domoService.generateNewReport("report-teste");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_report.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
