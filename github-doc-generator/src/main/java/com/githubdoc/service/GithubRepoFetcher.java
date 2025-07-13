package com.githubdoc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import java.util.Base64;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

@Service
public class GithubRepoFetcher {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GithubRepoFetcher() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "SpringBoot-GitHubDocGen")
                .build();
    }

    public String fetchRepoAsText(String githubUrl) {
        // Parse owner and repo from URL
        String[] parts = githubUrl.replace("https://github.com/", "").split("/");
        if (parts.length < 2) throw new IllegalArgumentException("Invalid GitHub URL");
        String owner = parts[0];
        String repo = parts[1];

        // 1. Find default branch
        String repoApiUrl = "https://api.github.com/repos/" + owner + "/" + repo;
        String defaultBranch = webClient.get()
                .uri(repoApiUrl)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("default_branch").asText())
                .block();

        // 2. Get the file tree (recursive)
        String treeApiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/git/trees/" + defaultBranch + "?recursive=1";
        JsonNode treeJson = webClient.get()
                .uri(treeApiUrl)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        StringBuilder repoText = new StringBuilder();
        Iterator<JsonNode> elements = treeJson.get("tree").elements();
        while (elements.hasNext()) {
            JsonNode node = elements.next();
            if ("blob".equals(node.get("type").asText())) {
                String path = node.get("path").asText();
                // Only fetch text files (skip binaries by extension, e.g., images, etc.)
                if (isTextFile(path)) {
                    String fileContent = fetchFileContent(owner, repo, path);
                    repoText.append("File: ").append(path).append("\n");
                    repoText.append(fileContent).append("\n\n");
                }
            }
        }
        return repoText.toString();
    }

    private String fetchFileContent(String owner, String repo, String path) {
        String fileApiUrl = UriComponentsBuilder.fromUriString("https://api.github.com/repos/{owner}/{repo}/contents/{path}")
                .buildAndExpand(owner, repo, path)
                .toUriString();
        JsonNode fileJson = webClient.get()
                .uri(fileApiUrl)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (fileJson.has("encoding") && "base64".equals(fileJson.get("encoding").asText())) {
            String content = fileJson.get("content").asText();
            // Remove line breaks in base64 (GitHub sometimes inserts them)
            content = content.replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(content);
            return new String(decoded);
        }
        return "";
    }

    // Simple check for text files (customize as needed)
    private boolean isTextFile(String path) {
        String[] textExtensions = { ".java", ".js", ".ts", ".py", ".md", ".txt", ".json", ".xml", ".yml", ".yaml", ".html", ".css", ".properties", ".c", ".cpp", ".h", ".rb", ".go", ".rs", ".php", ".sh", ".bat", ".ini", ".cfg" };
        for (String ext : textExtensions) {
            if (path.toLowerCase().endsWith(ext)) return true;
        }
        return false;
    }
}
