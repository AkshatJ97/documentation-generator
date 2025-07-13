import React from 'react';
import { TextField, Button } from '@mui/material';

const GithubUrlForm = (props) => (
  <form onSubmit={props.onSubmit} style={{ display: 'flex', gap: 8, marginBottom: 32 }}>
    <TextField
      label="GitHub Repo URL"
      variant="outlined"
      value={props.githubUrl}
      onChange={props.onChange}
      required
      sx={{ minWidth: 350 }}
    />
    <Button
      variant="contained"
      color="primary"
      type="submit"
      disabled={props.loading}
      sx={{ minWidth: 120 }}
    >
      {props.loading ? (
        <>
          Generating
          <span className="dots">
            <span>.</span>
            <span>.</span>
            <span>.</span>
          </span>
        </>
      ) : (
        "Submit"
      )}
    </Button>
    <style>{`
      .dots span {
        animation: blink 1.4s infinite both;
      }
      .dots span:nth-child(2) { animation-delay: .2s; }
      .dots span:nth-child(3) { animation-delay: .4s; }
      @keyframes blink {
        0%, 20% { opacity: 0; }
        50% { opacity: 1; }
      }
    `}</style>
  </form>
);

export default GithubUrlForm;
