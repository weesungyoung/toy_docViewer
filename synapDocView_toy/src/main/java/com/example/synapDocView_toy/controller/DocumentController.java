package com.example.synapDocView_toy.controller;

import com.example.synapDocView_toy.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Controller
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/api/documents/upload")
    @ResponseBody
    public String uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            return documentService.uploadDocument(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload file";
        }
    }

    @GetMapping("/api/documents/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path path = documentService.getDocumentPath(filename);
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/view")
    public String viewDocument(@RequestParam String filename, Model model) {
        model.addAttribute("filename", filename);
        return "view";  // This should map to src/main/resources/templates/view.html
    }
}