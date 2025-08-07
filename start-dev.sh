#!/bin/bash

echo "Starting development environment..."
echo "================================="
echo ""

# Compile LESS to CSS first
echo "Compiling LESS to CSS..."
npx lessc styles/main.less public/css/main.css
echo "✓ CSS compiled"
echo ""

# Start LESS watcher in background
echo "Starting LESS watcher..."
npm run less:watch &
LESS_PID=$!
echo "✓ LESS watcher started (PID: $LESS_PID)"
echo ""

# Start Shadow CLJS
echo "Starting Shadow CLJS..."
npm start

# When Shadow CLJS exits, kill the LESS watcher
kill $LESS_PID 2>/dev/null
echo "Development environment stopped."