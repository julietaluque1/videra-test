#!/bin/bash

# Create backup directory with timestamp
BACKUP_DIR="backup_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"

echo "Creating backup in $BACKUP_DIR..."

# Copy all important directories
cp -r src "$BACKUP_DIR/"
cp -r styles "$BACKUP_DIR/"
cp -r public "$BACKUP_DIR/"

# Copy important files
cp package.json "$BACKUP_DIR/"
cp shadow-cljs.edn "$BACKUP_DIR/"
cp BACKUP_SUMMARY.md "$BACKUP_DIR/"

# Create a list of all files
find src styles public -type f > "$BACKUP_DIR/file_list.txt"

echo "Backup completed in $BACKUP_DIR"
echo ""
echo "To save everything in git:"
echo "  git add ."
echo "  git commit -m 'Complete UI implementation with all components'"
echo ""
echo "To create a zip backup:"
echo "  zip -r my-project-backup-$(date +%Y%m%d).zip $BACKUP_DIR"