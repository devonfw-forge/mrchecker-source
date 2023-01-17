package com.capgemini.mrchecker.test.core.utils;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.Status;
import io.qameta.allure.util.ResultsUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardOpenOption.APPEND;

public class StepLogger {
    protected StepLogger() {
    }

    public static void step(String step) {
        step(step, Status.PASSED);
    }

    public static void error(String error) {
        step(error, Status.FAILED);
    }

    public static void warning(String warning) {
        step(warning, Status.BROKEN);
    }

    public static void info(String info) {
        step("[INFO] " + info);
    }

    public static void step(String step, Status status) {
        switch (status) {
            case BROKEN:
                BFLogger.logWarning(step);
                break;
            case FAILED:
                BFLogger.logError(step);
                break;
            default:
                BFLogger.logInfo(step);
        }
        Allure.step(step, status);
    }

    public static void issue(String name, String url) {
        link(ResultsUtils.ISSUE_LINK_TYPE, name, url);
    }

    public static void tmsLink(String name, String url) {
        link(ResultsUtils.TMS_LINK_TYPE, name, url);
    }

    public static void link(String type, String name, String url) {
        try {
            Allure.addLinks(new Link().setType(type).setName(name).setUrl(url));
        } catch (NullPointerException e) {
            // Catch when no allure report
            step(MessageFormat.format("[{0}][{1}] {2}", type, name, url));
        }
    }

    @Step("{step}")
    public static void stepsTree(String step, List<String> subSteps) {
        for (String s : subSteps) {
            step(s);
        }
    }

    @Step("{step}")
    public static void stepsTree(String step, List<String> subSteps, Status status) {
        for (String s : subSteps) {
            step(s, status);
        }
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String saveTextAttachmentToLog(String attachName, String message) {
        BFLogger.logInfo("Saved attachment: " + attachName);
        return message;
    }

    @Attachment(value = "{name}", type = "text/csv")
    public static byte[] attachCSVFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static byte[] attachTXTFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment("Zipped [{name}]")
    public static byte[] attachZippedFile(File fileToAttach, String name) throws IOException {
        String tempPath = System.getProperty("java.io.tmpdir");
        String zipFileName = "attachement.zip";
        File zipFile = new File(tempPath, zipFileName);
        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(
                fos); FileInputStream fis = new FileInputStream(fileToAttach)) {
            zos.putNextEntry(new ZipEntry(fileToAttach.getName()));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
        return Files.readAllBytes(Paths.get(zipFile.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "application/pdf")
    public static byte[] attachPDFFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachPNGFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "{name}", type = "application/zip")
    public static byte[] attachZIPFile(File file, String name) throws IOException {
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    public static void attachFile(File file, String name) throws IOException {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (file.length() == 0) {
            Files.write(Paths.get(file.toURI()), " ".getBytes(), APPEND);
        }
        switch (extension.toLowerCase()) {
            case "pdf": {
                attachPDFFile(file, name);
                break;
            }
            case "xlsx": {
                attachZippedFile(file, name);
                break;
            }
            case "csv": {
                attachCSVFile(file, name);
                break;
            }
            case "png": {
                attachPNGFile(file, name);
                break;
            }
            case "zip": {
                attachZIPFile(file, name);
                break;
            }
            case "txt": {
                attachTXTFile(file, name);
                break;
            }
            default:
                error("Couldn't attach file with extension: " + extension);
        }
    }
}