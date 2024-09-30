package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Value("${upload.path}")
    private String uploadDir;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam("fname") String fname,
            @RequestParam("sname") String sname,
            @RequestParam("gmail") String gmail,
            @RequestParam("image") MultipartFile image) {

        if (userService.existsByGmail(gmail)) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }

        // Store the image file
        String imagePath = saveImageFile(image);

        User user = new User(null, fname, sname, gmail, imagePath);
        User savedUser = userService.saveUser(user);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /*private String saveImageFile(MultipartFile file) {
        try {
            // Create the uploads directory if it doesn't exist
            File uploadsDir = new File(uploadDir);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs(); // Create the directory
            }

            String filename = file.getOriginalFilename();
            String filePath = uploadsDir.getAbsolutePath() + File.separator + filename;
            File dest = new File(filePath);
            file.transferTo(dest);
            return "/uploads/" + filename; // Return the relative path
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }*/



    private String saveImageFile(MultipartFile file) {
        try {
            // Create the uploads directory if it doesn't exist
            File uploadsDir = new File(uploadDir);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs(); // Create the directory
            }
    
            String filename = file.getOriginalFilename();
            String filePath = uploadsDir.getAbsolutePath() + File.separator + filename;
            File dest = new File(filePath);
            file.transferTo(dest);
            return filename; // Return just the filename
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
    
}
