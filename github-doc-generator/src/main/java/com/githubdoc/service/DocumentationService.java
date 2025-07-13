package com.githubdoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DocumentationService {

    private final PerplexityApiService perplexityApiService;

    public DocumentationService(PerplexityApiService perplexityApiService) {
        this.perplexityApiService = perplexityApiService;
    }

    @Autowired
    private GithubRepoFetcher githubRepoFetcher;

    public String generateDocumentation(String githubUrl) {
        String repoText = githubRepoFetcher.fetchRepoAsText(githubUrl);
        String prompt = buildPrompt(repoText);
        return perplexityApiService.getDocumentation(prompt);
    }

    private String fetchTextFromUrl(String url) {
        WebClient client = WebClient.create();
        return client.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String buildPrompt(String repoText) {
        return "Given the following GitHub repository content, generate a detailed, professional README.md file that includes:\n"
                + "- What the project does\n"
                + "- How it works (main logic)\n"
                + "- The project tree structure\n"
                + "- Tech stack\n"
                + "- Usage instructions (if possible)\n"
                + "- Any other useful details for a recruiter\n\n"
                + "Repository content:\n"
                + repoText;
    }
}
