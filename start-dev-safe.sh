#!/bin/bash

echo "🛡️  Safe Development Mode"
echo "========================"
echo ""

# Create backup directory
mkdir -p public/css/backups

# Initial backup if CSS exists
if [ -f "public/css/main.css" ]; then
    echo "📁 Creating initial backup..."
    cp public/css/main.css "public/css/backups/main.css.startup_$(date +%Y%m%d_%H%M%S)"
    echo "✅ Backup created"
fi

# Compile LESS first (safely)
echo ""
echo "🔄 Initial CSS compilation..."
./safe-compile-css.sh

echo ""
echo "🚀 Starting development servers..."
echo ""
echo "NOTE: Your CSS is protected!"
echo "- Every change creates a backup"
echo "- Backups are in public/css/backups/"
echo "- Original CSS is never lost"
echo ""

# Start Shadow CLJS only (no LESS watcher by default)
echo "Starting Shadow CLJS..."
npm start