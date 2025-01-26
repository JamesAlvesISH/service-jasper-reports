package br.com.jamesalves.demo.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class DemoService {
    public byte[] generateUserReport(String reportName) throws JRException {
        String format = "pdf";

        InputStream reportStream = getClass().getResourceAsStream("/reports/sample_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if ("pdf".equalsIgnoreCase(format)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } else if ("docx".equalsIgnoreCase(format)) {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
        }

        return outputStream.toByteArray();
    }

    private static final String REPORTS_DIRECTORY = "/reports/";

    public void generateReport() {
        try {
            File reportsDir = new File(REPORTS_DIRECTORY);
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            InputStream reportStream = getClass().getResourceAsStream("/reports/sample_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            String outputPath = REPORTS_DIRECTORY + "sample_report.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            System.out.println("Relatório salvo em: " + outputPath);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
