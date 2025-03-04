package com.weve.controller;

import com.weve.service.MessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/send")
    public String sendSMS(@RequestParam String phone) {
        return messageService.sendSMS(phone);
    }
}
