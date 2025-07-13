import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Card,
  CardContent,
  Typography,
  IconButton,
  Tooltip
} from '@mui/material';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';

const ReadmeDialog = (props) => (
  <Dialog open={props.open} onClose={props.onClose} maxWidth="md" fullWidth>
    <DialogTitle>
      Generated Documentation
      <Tooltip title={props.copyTooltip} arrow>
        <IconButton
          aria-label="copy"
          onClick={props.onCopy}
          sx={{ float: 'right', mt: -1, mr: -1 }}
        >
          <ContentCopyIcon />
        </IconButton>
      </Tooltip>
    </DialogTitle>
    <DialogContent>
      <Card variant="outlined" sx={{ whiteSpace: 'pre-wrap', fontFamily: 'monospace' }}>
        <CardContent>
          <Typography variant="body2">
            {props.readme}
          </Typography>
        </CardContent>
      </Card>
    </DialogContent>
  </Dialog>
);

export default ReadmeDialog;
