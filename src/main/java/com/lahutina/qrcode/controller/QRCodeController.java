package com.lahutina.qrcode.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class QRCodeController {

    @ResponseBody
    @PostMapping(value = "/generateQRCode")
    public String generateQRCode(@RequestParam("input") String input) {

        int width = 300, height = 300;
        String format = "png";

        try {
            // Create QRCodeWriter instance
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // Generate BitMatrix representing the QR code
            BitMatrix bitMatrix = qrCodeWriter.encode(input, BarcodeFormat.QR_CODE, width, height);

            // Create output stream for the QR code image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Write the QR code image to the output stream
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);

            // Get the byte array from the output stream
            byte[] imageData = outputStream.toByteArray();

            // Encode the byte array as Base64 and return the result
            return Base64.getEncoder().encodeToString(imageData);

        } catch (WriterException | IOException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            return "Error generating QR code";
        }
    }

    @ResponseBody
    @PostMapping("/processQRCode")
    public String processQRCode(@RequestBody String imageData) {
        try {
            // Remove the data URI prefix from the image data
            String base64Data = imageData.replaceFirst("^data:image/[a-zA-Z]+;base64,", "");

            // Decode the base64-encoded image data
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Convert the image bytes to a BufferedImage
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);

            // Convert the image to a binary bitmap
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Use ZXing library to decode the QR code
            QRCodeReader reader = new QRCodeReader();

            return reader.decode(binaryBitmap).getText();
        } catch (ChecksumException | NotFoundException | FormatException | IOException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            return "Error processing QR code.";
        }
    }
}
