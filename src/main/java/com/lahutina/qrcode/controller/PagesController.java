package com.lahutina.qrcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/qr-reader")
    public String qrReader() {
        return "qr-reader";
    }

    @GetMapping("/qr-generator")
    public String qrGenerator() {
        return "qr-generator";
    }
}
