package com.junlajobs_backend.controller;

import com.junlajobs_backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadPicController {

    @Autowired
    private FileService fileService;

    @PostMapping("/profilepic")
    public Object uploadProfilepic(@RequestParam("file") MultipartFile multipartFile) {
        return fileService.uploadProfilePic(multipartFile);
    }

    @PostMapping("/portpic/{portid}")
    public Object uploadProfilepic(@RequestParam("file") MultipartFile multipartFile,@PathVariable(value = "portid") String portId) {
        return fileService.uploadPortPic(multipartFile,portId);
    }

    @PostMapping("/recpic/{recid}")
    public Object uploadRecpic(@RequestParam("file") MultipartFile multipartFile,@PathVariable(value = "recid") String recId) {
        return fileService.uploadRecruitPic(multipartFile,recId);
    }
}
