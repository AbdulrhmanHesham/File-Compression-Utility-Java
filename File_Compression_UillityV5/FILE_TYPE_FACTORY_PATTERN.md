# File Type Factory Pattern - Implementation Guide

## Overview
The File Type Factory Pattern is implemented in the File Compression Utility to handle different file types (Text, Images, Videos) with specialized processing strategies.

## Design Pattern: Factory Pattern

### Purpose
The Factory Pattern provides an interface for creating objects (FileTypeProcessors) without specifying their exact classes. The factory decides which processor to instantiate based on the file extension.

### Components

#### 1. **FileTypeProcessor (Interface)**
- Located: `FileTypeProcessor.java`
- Defines the contract that all file type processors must follow
- Methods:
  - `byte[] prepareForCompression(byte[] data)` - Processes file data before compression
  - `String getFileType()` - Returns the type category (e.g., "Text", "Image", "Video")
  - `String getProcessingDescription()` - Describes what processing is done

#### 2. **FileTypeFactory (Factory Class)**
- Located: `FileTypeFactory.java`
- **Core Factory Method**: `getProcessor(String fileType)`
  - Takes a file extension as input
  - Returns the appropriate FileTypeProcessor implementation
  - Never returns null - uses DefaultProcessor for unknown types
  
- **Supported File Types**:
  - **Text**: txt, log, md, xml, json, csv, html, css, js, java, py, c, cpp, h
  - **Image**: png, jpg, jpeg, gif, bmp, tiff, webp
  - **Video**: mp4, avi, mkv, mov, wmv, flv, webm
  - **Generic**: All other extensions

#### 3. **Concrete Processors**

##### TextProcessor
- **Processing Logic**:
  - Normalizes line endings to Unix style (\n)
  - Removes trailing whitespace from each line
  - Reduces multiple consecutive blank lines to maximum of 2
- **Benefit**: Better compression ratios for text files

##### ImageProcessor
- **Processing Logic**:
  - Reads image metadata (dimensions)
  - Validates image format
  - Logs image information to console
- **Benefit**: Validates image integrity and provides useful metadata
- **Note**: Image data is not modified (already optimized formats)

##### VideoProcessor
- **Processing Logic**:
  - Detects video format using magic numbers/signatures
  - Supports: MP4, AVI, MKV detection
  - Logs video file information
- **Benefit**: Validates video format without modification
- **Note**: Videos are not re-encoded (would be lossy and slow)

##### DefaultProcessor
- **Processing Logic**: No preprocessing
- **Purpose**: Handles unknown/generic binary files safely
- **Benefit**: Ensures the factory always returns a valid processor

## How It Works

### Single File Compression Flow
1. User selects a file through the GUI
2. `FileTypeFactory.getFileExtension()` extracts the extension
3. `FileTypeFactory.getProcessor()` returns the appropriate processor
4. The processor's type and description are logged to console
5. File data is read using `FileHandler`
6. `processor.prepareForCompression()` is called to optimize the data
7. The file is compressed using the selected algorithm (ZIP/RAR)

### Multiple File Compression Flow
1. User selects multiple files
2. Each file is analyzed to determine its type
3. Console displays a list showing each file and its processor type
4. Files are compressed together into a single archive

## Usage Example

```java
// Get the file extension
String extension = FileTypeFactory.getFileExtension("document.txt");

// Factory creates the appropriate processor
FileTypeProcessor processor = FileTypeFactory.getProcessor(extension);

// Use the processor
System.out.println("Processing as: " + processor.getFileType());
System.out.println("Method: " + processor.getProcessingDescription());

byte[] originalData = readFile("document.txt");
byte[] processedData = processor.prepareForCompression(originalData);
```

## Demonstration

Run `FileTypeFactoryDemo.java` to see the factory pattern in action:
- Demonstrates processing of various file types
- Shows how different processors handle different extensions
- Displays before/after data sizes

## Benefits of This Implementation

1. **Extensibility**: Easy to add new file type processors
2. **Maintainability**: Each processor is in its own class
3. **Type Safety**: Never returns null, always returns a valid processor
4. **Flexibility**: Different processing strategies for different file types
5. **Separation of Concerns**: File type detection is separate from processing logic

## Console Output Example

When compressing a file, you'll see output like:
```
=== FILE TYPE PROCESSING ===
File: example.txt
Extension: txt
Processor Type: Text
Processing: Normalizes line endings, removes trailing whitespace, and reduces blank lines
============================

TextProcessor: Processing text file
Compressing...
```

## Integration Points

The Factory Pattern is integrated into:
- **FileSelectionPanel.java**: Lines 234-261 (single file) and 266-278 (multiple files)
- Triggered when the "COMPRESS" button is clicked
- Works with both ZIP and RAR compression types

## Future Enhancements

Potential improvements:
1. Add more file types (PDF, Audio, Archives)
2. Implement actual image optimization (resize, quality adjustment)
3. Add file type statistics to GUI
4. Create compression reports showing processing results
5. Add user-configurable processing options

## Design Pattern Benefits Demonstrated

✓ **Open/Closed Principle**: New processors can be added without modifying existing code
✓ **Single Responsibility**: Each processor handles one file type
✓ **Dependency Inversion**: Code depends on FileTypeProcessor interface, not concrete implementations
✓ **Factory Pattern**: Centralizes object creation logic in FileTypeFactory
