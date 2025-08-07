#!/bin/bash

# Safe CSS Compilation Script
# This ensures you NEVER lose your CSS

echo "Safe CSS Compilation"
echo "==================="
echo ""

# Create backup directory if it doesn't exist
mkdir -p public/css/backups

# Check if main.css exists
if [ -f "public/css/main.css" ]; then
    # Create timestamped backup
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    BACKUP_FILE="public/css/backups/main.css.backup_${TIMESTAMP}"
    
    echo "📁 Creating backup: $BACKUP_FILE"
    cp public/css/main.css "$BACKUP_FILE"
    echo "✅ Backup created"
    echo ""
fi

# Compile LESS to CSS
echo "🔄 Compiling LESS to CSS..."
npx lessc styles/main.less public/css/main.css.new

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "✅ Compilation successful"
    
    # Move new file to main.css
    mv public/css/main.css.new public/css/main.css
    echo "✅ CSS updated"
    
    # Keep only last 10 backups
    cd public/css/backups
    ls -t main.css.backup_* 2>/dev/null | tail -n +11 | xargs rm -f 2>/dev/null
    cd ../../..
    
    echo ""
    echo "✨ Done! Your CSS is safe and updated."
else
    echo "❌ Compilation failed"
    echo "Your original CSS is untouched at public/css/main.css"
    rm -f public/css/main.css.new
    exit 1
fi