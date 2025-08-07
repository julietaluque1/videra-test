#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { exec } = require('child_process');
const chokidar = require('chokidar');

console.log('Safe LESS Watcher Started');
console.log('========================');
console.log('Watching: styles/main.less');
console.log('Output: public/css/main.css');
console.log('');

// Watch the LESS file
const watcher = chokidar.watch('styles/main.less', {
  persistent: true,
  ignoreInitial: true
});

watcher.on('change', (filepath) => {
  console.log(`\n[${new Date().toLocaleTimeString()}] LESS file changed`);
  
  // Run the safe compile script
  exec('./safe-compile-css.sh', (error, stdout, stderr) => {
    if (error) {
      console.error('❌ Compilation error:', error.message);
      return;
    }
    
    console.log(stdout);
    
    if (stderr) {
      console.error('Warnings:', stderr);
    }
  });
});

console.log('✅ Watcher ready. Press Ctrl+C to stop.\n');

// Handle exit
process.on('SIGINT', () => {
  console.log('\n👋 Stopping watcher...');
  watcher.close();
  process.exit();
});