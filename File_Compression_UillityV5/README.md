# File Compression Utility V5

A Java-based file compression utility with design pattern implementations including **Factory Pattern** for file type handling.

## 🎯 Key Features

### File Compression
- **Multiple Formats**: ZIP and RAR compression support
- **Batch Processing**: Compress single or multiple files at once
- **Archive Naming**: Smart archive naming with timestamps (Builder Pattern)

### Design Patterns Implemented

#### 1. **Factory Pattern** - File Type Processing ⭐ NEW
- Handles different file types (Text, Images, Videos) with specialized processors
- 30+ supported file extensions
- Type-specific preprocessing for optimal compression
- **See**: `FILE_TYPE_FACTORY_PATTERN.md` for details

#### 2. Singleton Pattern
- `CompressionManager`: Single instance for managing compressions
- `FileHandler`: Centralized file operations

#### 3. Adapter Pattern
- `RarCompressorAdapter`: Adapts RAR compression to common interface

#### 4. Builder Pattern
- `ArchiveNameBuilder`: Constructs archive names step-by-step

#### 5. Prototype Pattern
- `FileSelection`: Clones file selection configurations

## 🚀 Quick Start

### Running the Application
```bash
# Compile
javac -d out src/file_compression_uillity/*.java

# Run GUI
java -cp out file_compression_uillity.FileSelectionPanel

# Run Factory Pattern Demo
java -cp out file_compression_uillity.FileTypeFactoryDemo
```

### Using the GUI
1. Click **Browse** to select file(s)
2. Choose compression type (ZIP/RAR)
3. Click **COMPRESS**
4. Watch the console for detailed processing information!

## 📝 File Type Factory Pattern

The Factory Pattern automatically detects file types and applies appropriate preprocessing:

### Supported File Types

| Category | Extensions | Processing |
|----------|-----------|------------|
| **Text** | txt, log, md, json, java, py, etc. | Line ending normalization, whitespace cleanup |
| **Image** | jpg, png, gif, bmp, etc. | Metadata extraction, validation |
| **Video** | mp4, avi, mkv, mov, etc. | Format detection, validation |
| **Generic** | All others | Safe pass-through |

### Example Console Output
```
=== FILE TYPE PROCESSING ===
File: example.txt
Extension: txt
Processor Type: Text
Processing: Normalizes line endings, removes trailing whitespace, and reduces blank lines
============================

TextProcessor: Processing text file...
Compressing Text file...
Compression completed successfully!
```

## 📚 Documentation

| Document | Description |
|----------|-------------|
| `FILE_TYPE_FACTORY_PATTERN.md` | Complete implementation guide |
| `FACTORY_PATTERN_DIAGRAMS.md` | Visual diagrams and architecture |
| `IMPLEMENTATION_SUMMARY.md` | What was implemented and how |
| `QUICK_REFERENCE.txt` | Quick lookup reference card |

## 🏗️ Project Structure

```
src/file_compression_uillity/
├── Core Interfaces
│   ├── FileTypeProcessor.java      (Factory Pattern Interface)
│   └── Compressor.java
│
├── Factory Pattern Implementation
│   ├── FileTypeFactory.java        (Factory)
│   ├── TextProcessor.java          (Text files)
│   ├── ImageProcessor.java         (Images)
│   ├── VideoProcessor.java         (Videos)
│   └── DefaultProcessor.java       (Generic files)
│
├── Compression
│   ├── CompressionManager.java     (Singleton)
│   ├── ZipCompressor.java
│   └── RarCompressorAdapter.java   (Adapter Pattern)
│
├── Builders & Utilities
│   ├── ArchiveNameBuilder.java     (Builder Pattern)
│   ├── FileSelection.java          (Prototype Pattern)
│   └── FileHandler.java            (Singleton)
│
└── GUI
    └── FileSelectionPanel.java
```

## 🎓 Design Pattern Benefits

### Factory Pattern (File Types)
- ✅ **Extensibility**: Easy to add new file type processors
- ✅ **Maintainability**: Each processor is in its own class
- ✅ **Type Safety**: Never returns null
- ✅ **Flexibility**: Different strategies for different file types

### Other Patterns
- **Singleton**: Ensures single instance of managers
- **Adapter**: Integrates RAR without changing interface
- **Builder**: Clean archive name construction
- **Prototype**: Clone file selection configurations

## 🧪 Testing the Factory Pattern

Run the demonstration program:
```bash
java -cp out file_compression_uillity.FileTypeFactoryDemo
```

This will show how different file types (txt, jpg, mp4, json, etc.) are processed by different processors.

## 💡 Code Example

```java
// Get file extension
String extension = FileTypeFactory.getFileExtension("document.txt");

// Factory creates appropriate processor
FileTypeProcessor processor = FileTypeFactory.getProcessor(extension);

// Use the processor
System.out.println("Type: " + processor.getFileType());
System.out.println("Processing: " + processor.getProcessingDescription());

byte[] rawData = readFile("document.txt");
byte[] processed = processor.prepareForCompression(rawData);
```

## 🔧 Requirements

- Java 8 or higher
- NetBeans IDE (for GUI editing - optional)

## 📖 How It Works

1. **File Selection**: User selects files via GUI
2. **Type Detection**: Factory identifies file type by extension
3. **Processor Creation**: Factory returns appropriate processor
4. **Preprocessing**: Processor optimizes file data
5. **Compression**: Files are compressed using selected algorithm
6. **Output**: Compressed archive is created

## 🌟 Recent Updates

### File Type Factory Pattern (Latest)
- ✅ Implemented functional TextProcessor with optimization
- ✅ Enhanced ImageProcessor with metadata extraction
- ✅ Added VideoProcessor with format detection
- ✅ Created DefaultProcessor for unknown types
- ✅ Expanded factory to support 30+ file extensions
- ✅ Integrated into GUI with user feedback
- ✅ Created comprehensive documentation

## 👨‍💻 Development

Built with design patterns in mind for maintainability and extensibility. Each pattern serves a specific purpose and can be studied independently.

## 📄 License

Educational project demonstrating design patterns in Java.

---

**Note**: When compressing files, check the console output to see the Factory Pattern in action! Each file type will show its specific processor and preprocessing steps.
