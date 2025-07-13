import React, { useState } from 'react';
import { Box, Typography } from '@mui/material';
import GithubUrlForm from './components/GithubUrlForm';
import ReadmeDialog from './components/ReadmeDialog';

const BACKEND_URL = "http://localhost:8081/api/documentation"; // Change if needed

function App() {
  const [githubUrl, setGithubUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [readme, setReadme] = useState('');
  const [copyTooltip, setCopyTooltip] = useState("Copy");

  const handleChange = (e) => setGithubUrl(e.target.value);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setReadme('');
    try {
      const response = await fetch(BACKEND_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ githubUrl })
      });
      const text = await response.text();
      setReadme(text);
      console.log('API Response:',text);
    } catch (err) {
      setReadme('Error: ' + err.message);
      console.log('Api Error:',err)
    }
    setLoading(false);
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(readme);
    setCopyTooltip("Copied!");
    setTimeout(() => setCopyTooltip("Copy"), 1200);
  };

  // Dialog is open only if readme is non-empty
  const handleDialogClose = () => setReadme('');

  return (
    <Box
      sx={{
        minHeight: '100vh',
        bgcolor: '#f7f7f7',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        p: 2
      }}
    >
      <Typography variant="h4" gutterBottom>
        GitHub Documentation Generator
      </Typography>
      <GithubUrlForm
        githubUrl={githubUrl}
        onChange={handleChange}
        onSubmit={handleSubmit}
        loading={loading}
      />
      <ReadmeDialog
        open={!!readme}
        onClose={handleDialogClose}
        readme={readme && readme.trim()? readme : "No documentation generated or an error occured."}
        onCopy={handleCopy}
        copyTooltip={copyTooltip}
      />
    </Box>
  );
}

export default App;
