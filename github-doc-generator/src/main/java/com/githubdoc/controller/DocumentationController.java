package com.githubdoc.controller;

import com.githubdoc.service.DocumentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documentation")
@CrossOrigin(origins = "*")
public class DocumentationController {

    @Autowired
    private DocumentationService documentationService;

    @PostMapping
    public String generateDocumentation(@RequestBody GithubUrlRequest request) {
        System.out.println(request.getGithubUrl());
        return documentationService.generateDocumentation(request.getGithubUrl());
    }

    public static class GithubUrlRequest {
        private String githubUrl;
        public String getGithubUrl() { return githubUrl; }
        public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    }
}
