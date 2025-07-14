# GitHub Documentation Generator

A web application to **automatically generate professional README.md documentation** for any public GitHub repository. Paste a repo URL, and receive a ready Markdown readme that includes core project details, logic, tech stack, file structure, and usage—all generated in seconds by AI.

---

## 🚀 What Does This Project Do?

**GitHub Documentation Generator** lets users input a GitHub repository URL and instantly receive a high-quality, detailed `README.md`. This tool is built to support developers, open source maintainers, and recruiters who need fast, consistent, and comprehensive repository documentation.

**You get:**
- An automated README with project description, logic, tech stack, file tree, and usage.
- Professional formatting, ready to copy-paste or commit to your repo.

---

## 🎥 Demo Project Video

[Watch Project Demo](https://www.loom.com/share/2e0619f2c92d4bd3808cdff88edb7a7f?sid=943016ea-dbcd-4c1e-a967-9b4b409ec77f)

---

## 🧠 How It Works (Main Logic)

1. **User submits a GitHub repo URL** via the frontend form.
2. **Backend fetches repository content**:
   - Determines the default branch.
   - Recursively downloads all text/source files (ignoring binaries).
   - Aggregates content and file paths.
3. **AI prompt assembly**:
   - The backend constructs a specific prompt describing what information to extract (project purpose, logic, tree, stack, usage, etc.).
4. **Perplexity AI API call**:
   - The prompt and content are sent to the Perplexity AI API.
   - The AI generates a detailed, Markdown-formatted README.
5. **Frontend displays the generated README.**
   - Users can preview, copy, or save the output.

---

## 🗂️ Project Structure

```
github-doc-generator-frontend/
  └── github-doc-generator/
      ├── public/
      │   ├── index.html
      │   ├── manifest.json
      │   └── ...
      ├── src/
      │   ├── App.js
      │   ├── App.css
      │   ├── index.js
      │   ├── index.css
      │   ├── components/
      │   │   ├── GithubUrlForm.js
      │   │   └── ReadmeDialog.js
      │   └── ...
      ├── package.json
      └── README.md

github-doc-generator/
  ├── src/
  │   └── main/java/com/githubdoc/
  │       ├── GithubDocGeneratorApplication.java
  │       ├── controller/DocumentationController.java
  │       ├── service/
  │       │   ├── DocumentationService.java
  │       │   ├── GithubRepoFetcher.java
  │       │   └── PerplexityApiService.java
  │       └── budget/BudgetTracker.java
  ├── pom.xml
  └── ... (resources, config)
```
---

## 🛠️ Tech Stack

| Layer         | Tech Used                                 | Purpose                                              |
|---------------|------------------------------------------|------------------------------------------------------|
| Frontend      | **React 18/19**<br>Material UI (MUI)     | Interactive, modern UI for URL input, output display |
| Styling       | CSS (custom + MUI)                       | Responsive styling                                   |
| API comms     | `fetch` (browser)                        | Frontend-backend data flow                           |
| Backend       | **Spring Boot 3 (Java 17)**              | REST API, business logic, web server                 |
| Integration   | **WebClient (Spring WebFlux)**           | Async HTTP to GitHub & Perplexity APIs               |
| Source fetch  | GitHub REST API                          | Fetches repo file tree, reads content                |
| AI Provider   | **Perplexity AI API**                    | LLM-powered README generation                        |
| Cost/Budget   | Custom budget tracking utility            | Prevents API overspend (configurable)                |
| Testing       | JUnit, React Testing Library             | Test coverage                                        |

---

## ⚡ Usage Instructions

### Prerequisites
- **Java 17+**
- **Node.js 18+** (for frontend)
- A **Perplexity AI API key** (set as `PERPLEXITY_API_KEY` env variable)

### 1. Start Backend

```bash
cd github-doc-generator
./mvnw spring-boot:run
# Runs at http://localhost:8081 by default
```

### 2. Start Frontend

```bash
cd github-doc-generator-frontend/github-doc-generator
npm install
npm start
# Opens at http://localhost:3000
```

### 3. Generate a README

- Open the UI.
- Paste a public GitHub repo URL (e.g., https://github.com/facebook/react).
- Click **Submit**.
- After processing, a dialog shows a professional README—copy or edit as needed.

---

## 📝 Configuration & Customization

- **API Keys:** Set `PERPLEXITY_API_KEY` and `budget.limit` in backend's `application.properties` as needed.
- **Budget:** The backend tracks Perplexity API usage and stops if over budget.
- **File Filtering:** Only common text/code files are processed (see `isTextFile` in backend).
- **Frontend/Backend URLs:** Can be configured in environment variables or code.

---

## 📣 Why This Project?

Modern recruiters and developers expect well-documented projects. Writing great README files is tedious and often neglected. This tool automates the process, ensuring every repo you share is impressive and complete.

**Useful for:**
- Job applications
- Open source project maintainers
- Quick onboarding for new team members
- Technical recruiters reviewing engineering portfolios

---
