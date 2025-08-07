# Development Guide - Preventing CSS Loss

## The Problem
When you save files while `less:watch` is running, it might overwrite your CSS changes if they weren't made in the LESS file.

## The Solution

### Option 1: Use the start-dev.sh script (Recommended)
```bash
./start-dev.sh
```
This script:
1. Compiles LESS to CSS first
2. Starts the LESS watcher
3. Starts Shadow CLJS
4. Ensures everything stays in sync

### Option 2: Run commands separately in different terminals

**Terminal 1 - ClojureScript:**
```bash
npm start
```

**Terminal 2 - LESS compilation (only when you change styles/main.less):**
```bash
npm run less:compile
```

### Option 3: Manual compilation
If you want full control:
```bash
# Start only ClojureScript (no LESS watching)
npm start

# When you change LESS files, manually compile:
npm run less:compile
```

## Important Files & Locations

- **LESS Source**: `styles/main.less`
- **Compiled CSS**: `public/css/main.css`
- **HTML Link**: `<link rel="stylesheet" href="/css/main.css" />`

## What Each Command Does

- `npm start` - Starts Shadow CLJS watcher (ClojureScript only)
- `npm run less:compile` - Compiles LESS to CSS once
- `npm run less:watch` - Watches LESS files and auto-compiles (can overwrite changes!)
- `./start-dev.sh` - Runs everything properly synchronized

## To Prevent Losing Changes

1. **Always edit styles in `styles/main.less`**, not in the compiled CSS
2. **Use the start-dev.sh script** for development
3. **If you must edit CSS directly**, stop the LESS watcher first

## Quick Start for Development

```bash
# First time setup
npm run less:compile

# Start development
./start-dev.sh

# Or if you prefer separate terminals:
# Terminal 1
npm start

# Terminal 2 (optional, only if changing LESS)
npm run less:watch
```

## Troubleshooting

**CSS changes disappear:**
- You're editing `public/css/main.css` directly while LESS watcher is running
- Solution: Edit `styles/main.less` instead

**Styles not updating:**
- LESS might not be compiling
- Solution: Run `npm run less:compile` manually

**Page shows no styles:**
- Check that index.html points to `/css/main.css`
- Make sure the file exists at `public/css/main.css`