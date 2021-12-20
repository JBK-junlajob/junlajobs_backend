package com.junlajobs_backend.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.model.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


@Service
public class FileService {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("junlajob-project.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./firebaseAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return "https://firebasestorage.googleapis.com/v0/b/junlajob-project.appspot.com/o/"+fileName+"?alt=media";
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public ResponseEntity<String> uploadProfilePic(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()+"profilepic"+this.getExtension(fileName);  // to generated random string values for file name.
            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            UserEntity user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            user.getUserDetail().setProfilePicUrl(TEMP_URL);
            userService.editUser(user.getUserDetail());
            return  ResponseEntity.ok(TEMP_URL);                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unsuccessfully Uploaded!");
        }

    }

    public ResponseEntity<String> uploadPortPic(MultipartFile multipartFile,String portId) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = portId+"portpic"+this.getExtension(fileName);  // to generated random string values for file name.
            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            PostEntity post = postService.getPort(portId);
            post.getPostDetail().setPicUrl(TEMP_URL);
            postService.editPortfolio(post);
            return  ResponseEntity.ok(TEMP_URL);                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unsuccessfully Uploaded!");
        }

    }

    public ResponseEntity<String> uploadRecruitPic(MultipartFile multipartFile,String recId) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = recId+"recpic"+this.getExtension(fileName);  // to generated random string values for file name.
            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            PostEntity post = postService.getRec(recId);
            post.getPostDetail().setPicUrl(TEMP_URL);
            postService.editRec(post);
            return  ResponseEntity.ok(TEMP_URL);                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unsuccessfully Uploaded!");
        }

    }


}
