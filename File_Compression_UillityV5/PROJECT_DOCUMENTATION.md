# File Compression Utility - Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture Overview](#architecture-overview)
3. [Design Patterns Used](#design-patterns-used)
4. [Class Descriptions](#class-descriptions)
5. [Pattern Justification](#pattern-justification)

---

## Project Overview

The **File Compression Utility** is a Java-based desktop application that allows users to compress and decompress files using different compression algorithms (ZIP and RAR). The application features a graphical user interface (GUI) built with Java Swing and implements multiple design patterns to ensure maintainability, extensibility, and code reusability.

### Key Features
- Support for multiple compression formats (ZIP, RAR)
- Single and multi-file compression
- File type-specific preprocessing (Text, Image, Video)
- User-friendly GUI with progress tracking
- Extensible architecture using design patterns

---

## Architecture Overview

The application follows a **layered architecture** with clear separation of concerns:

1. **Presentation Layer**: `FileSelectionPanel` (GUI)
2. **Business Logic Layer**: `CompressionManager`, Processors, Factories
3. **Data Access Layer**: `FileHandler`
4. **External Adapters**: `RarCompressorAdapter`

```
┌─────────────────────────────────────────┐
│     FileSelectionPanel (GUI)            │
├─────────────────────────────────────────┤
│  CompressionManager (Singleton)         │
├─────────────────────────────────────────┤
│  CompressionFactory │ FileTypeFactory   │
├─────────────────────────────────────────┤
│  Compressor Implementations             │
│  - ZipCompressor                        │
│  - RarCompressorAdapter (Adapter)       │
├─────────────────────────────────────────┤
│  FileHandler (Singleton)                │
└─────────────────────────────────────────┘
```

---

## Design Patterns Used

The project implements **six major design patterns**:

1. **Singleton Pattern** - Used in `CompressionManager` and `FileHandler`
2. **Factory Pattern** - Two implementations:
   - `CompressionFactory` for creating compressor instances
   - `FileTypeFactory` for creating file type processors
3. **Adapter Pattern** - `RarCompressorAdapter` adapts external RAR tools
4. **Builder Pattern** - `ArchiveNameBuilder` for constructing archive filenames
5. **Prototype Pattern** - `FileSelection` for cloning file selections
6. **Strategy Pattern** (Interface-based) - `Compressor` and `FileTypeProcessor` interfaces

---

## Class Descriptions

### 1. Main Application Class

#### `File_Compression_Uillity`
- **Type**: Main Application Entry Point
- **Purpose**: Launches the application and initializes the GUI
- **Responsibilities**:
  - Creates and displays the `FileSelectionPanel`
  - Serves as the application entry point
- **Design Pattern**: None (Simple launcher)
- **Key Methods**:
  - `main(String[] args)`: Application entry point

---

### 2. Presentation Layer

#### `FileSelectionPanel`
- **Type**: GUI Component (extends `JFrame`)
- **Purpose**: Provides user interface for file selection, compression, and decompression
- **Responsibilities**:
  - Handle user interactions (browse, compress, decompress)
  - Display progress and status updates
  - Coordinate between GUI and business logic
  - Manage file selection using Prototype Pattern
- **Design Patterns Used**:
  - **Prototype Pattern**: Stores and clones `FileSelection` objects
  - **Factory Pattern**: Uses `CompressionFactory` and `FileTypeFactory`
  - **Builder Pattern**: Uses `ArchiveNameBuilder` for multi-file archives
- **Key Features**:
  - Multi-file selection support
  - Real-time progress bar
  - Status message display
  - File type detection and processing feedback
- **Key Methods**:
  - `BrowseButtonActionPerformed()`: Handles file browsing
  - `CompressButtonActionPerformed()`: Initiates compression
  - `DecompressButtonActionPerformed()`: Initiates decompression

---

### 3. Business Logic Layer

#### `CompressionManager`
- **Type**: Singleton Service Class
- **Purpose**: Centralized manager for all compression/decompression operations
- **Responsibilities**:
  - Coordinate compression and decompression workflows
  - Manage progress updates
  - Validate file existence
  - Handle single and multi-file operations
- **Design Pattern**: **Singleton Pattern** (Lazy Initialization)
- **Justification**: 
  - Only one instance needed throughout the application
  - Provides global access point for compression operations
  - Ensures consistent state management
- **Key Methods**:
  - `getInstance()`: Returns the singleton instance
  - `compressFile(filePath, compressor, progressBar)`: Compress single file
  - `decompressFile(filePath, compressor, progressBar)`: Decompress archive
  - `compressFiles(filePaths, outputPath, compressor, progressBar)`: Compress multiple files

---

### 4. Factory Pattern Classes

#### `CompressionFactory`
- **Type**: Factory Class (Static Factory Method)
- **Purpose**: Creates appropriate `Compressor` instances based on compression type
- **Responsibilities**:
  - Instantiate `ZipCompressor` for ZIP compression
  - Instantiate `RarCompressorAdapter` for RAR compression
  - Provide centralized object creation
- **Design Pattern**: **Factory Pattern**
- **Justification**:
  - Encapsulates object creation logic
  - Allows adding new compression types without modifying client code
  - Provides loose coupling between client and concrete implementations
- **Key Methods**:
  - `createCompressor(String type)`: Returns appropriate compressor instance

#### `FileTypeFactory`
- **Type**: Factory Class (Static Factory Method)
- **Purpose**: Creates appropriate `FileTypeProcessor` instances based on file extension
- **Responsibilities**:
  - Map file extensions to processor types
  - Instantiate appropriate processor (Text, Image, Video, Default)
  - Extract file extensions from file paths
- **Design Pattern**: **Factory Pattern**
- **Justification**:
  - Separates file type detection from processing logic
  - Easily extensible for new file types
  - Provides default handler for unknown types
  - Centralizes file extension mapping
- **Supported File Types**:
  - **Text**: txt, log, md, xml, json, csv, html, css, js, java, py, c, cpp, h
  - **Image**: png, jpg, jpeg, gif, bmp, tiff, webp
  - **Video**: mp4, avi, mkv, mov, wmv, flv, webm
  - **Default**: All other file types
- **Key Methods**:
  - `getProcessor(String fileType)`: Returns appropriate processor
  - `getFileExtension(String filePath)`: Extracts file extension

---

### 5. Compression Strategy Classes

#### `Compressor` (Interface)
- **Type**: Strategy Interface
- **Purpose**: Defines the contract for all compression implementations
- **Responsibilities**:
  - Define compression/decompression interface
  - Support single and multi-file operations
  - Provide extension information
- **Design Pattern**: **Strategy Pattern** (Interface-based polymorphism)
- **Methods**:
  - `compress(String filePath)`: Compress a single file
  - `decompress(String filePath)`: Decompress an archive
  - `compressMultiple(String[] filePaths, String outputArchivePath)`: Compress multiple files
  - `getExtension()`: Get the file extension for this compression type

#### `ZipCompressor`
- **Type**: Concrete Strategy Implementation
- **Purpose**: Implements ZIP compression and decompression using Java's built-in libraries
- **Responsibilities**:
  - Create ZIP archives from files
  - Extract files from ZIP archives
  - Handle single and multi-file compression
- **Implementation Details**:
  - Uses `java.util.zip` package
  - Creates archives in the same directory as source files
  - Extracts to folders named after the archive
- **Key Methods**:
  - `compress(String filePath)`: Creates .zip archive
  - `decompress(String filePath)`: Extracts .zip archive
  - `compressMultiple(String[], String)`: Creates multi-file .zip archive
  - `getExtension()`: Returns ".zip"

#### `RarCompressorAdapter`
- **Type**: Adapter Class
- **Purpose**: Adapts external WinRAR command-line tools to work with the `Compressor` interface
- **Responsibilities**:
  - Execute external RAR.exe and UnRAR.exe processes
  - Translate method calls to command-line arguments
  - Handle process I/O and error codes
  - Support custom executable paths
- **Design Pattern**: **Adapter Pattern**
- **Justification**:
  - Integrates incompatible external tools (CLI) with internal interface
  - Allows using WinRAR without implementing RAR algorithm from scratch
  - Maintains consistent interface for all compression types
  - Easily swappable with other RAR implementations
- **Adapted Interface**: Command-line tools → Java `Compressor` interface
- **Default Paths**:
  - RAR: `C:\Program Files\WinRAR\Rar.exe`
  - UnRAR: `C:\Program Files\WinRAR\UnRAR.exe`
- **Key Methods**:
  - `compress(String)`: Executes `rar a -ep <archive> <file>`
  - `decompress(String)`: Executes `unrar e -y <archive> <output>`
  - `compressMultiple(String[], String)`: Executes `rar a -ep <archive> <file1> <file2>...`
  - `getExtension()`: Returns ".rar"

---

### 6. File Type Processing Classes

#### `FileTypeProcessor` (Interface)
- **Type**: Strategy Interface
- **Purpose**: Defines the contract for file type-specific preprocessing
- **Responsibilities**:
  - Define preprocessing interface
  - Provide file type identification
  - Describe processing operations
- **Design Pattern**: **Strategy Pattern** (Interface-based polymorphism)
- **Methods**:
  - `prepareForCompression(byte[] data)`: Process file data before compression
  - `getFileType()`: Return file type category
  - `getProcessingDescription()`: Describe what preprocessing does

#### `TextProcessor`
- **Type**: Concrete Processor Implementation
- **Purpose**: Optimizes text files for better compression
- **Responsibilities**:
  - Normalize line endings (convert to Unix-style \n)
  - Remove trailing whitespace from lines
  - Reduce excessive blank lines (max 2 consecutive)
- **Design Pattern**: Implements **Factory Pattern** product
- **Justification**: Text files benefit from normalization, which improves compression ratios
- **Processing Steps**:
  1. Convert to UTF-8 string
  2. Normalize line endings
  3. Trim trailing whitespace
  4. Reduce multiple blank lines
- **Key Methods**:
  - `prepareForCompression(byte[])`: Returns optimized text data
  - `getFileType()`: Returns "Text"

#### `ImageProcessor`
- **Type**: Concrete Processor Implementation
- **Purpose**: Validates image files and extracts metadata
- **Responsibilities**:
  - Read image dimensions (width, height)
  - Calculate pixel count
  - Log image information
  - Validate image format
- **Design Pattern**: Implements **Factory Pattern** product
- **Justification**: Images are already compressed; we validate and log metadata for user feedback
- **Processing Steps**:
  1. Read image using `ImageIO`
  2. Extract dimensions
  3. Log metadata
  4. Return original data (no modification)
- **Key Methods**:
  - `prepareForCompression(byte[])`: Validates and logs image info
  - `getFileType()`: Returns "Image"

#### `VideoProcessor`
- **Type**: Concrete Processor Implementation
- **Purpose**: Detects and validates video file formats
- **Responsibilities**:
  - Detect video format using file signatures (magic numbers)
  - Identify MP4, AVI, MKV formats
  - Log video file information
- **Design Pattern**: Implements **Factory Pattern** product
- **Justification**: Videos are already compressed; re-encoding would be lossy and complex
- **Supported Formats**:
  - **MP4**: Detects "ftyp" signature at bytes 4-7
  - **AVI**: Detects "RIFF" + "AVI " signature
  - **MKV**: Detects "matroska" in header
- **Processing Steps**:
  1. Read file signature
  2. Match against known patterns
  3. Log detected format
  4. Return original data (no modification)
- **Key Methods**:
  - `prepareForCompression(byte[])`: Validates video format
  - `detectVideoFormat(byte[])`: Identifies format from signature
  - `getFileType()`: Returns "Video"

#### `DefaultProcessor`
- **Type**: Default/Fallback Processor Implementation
- **Purpose**: Handles unknown or generic file types
- **Responsibilities**:
  - Process files with unknown extensions
  - Serve as fallback for unsupported types
  - Log file size
- **Design Pattern**: Implements **Factory Pattern** product (Default handler)
- **Justification**: Provides safe fallback for any file type, ensuring the application never crashes due to unknown formats
- **Processing Steps**:
  1. Log file size
  2. Return original data unchanged
- **Key Methods**:
  - `prepareForCompression(byte[])`: Returns data as-is
  - `getFileType()`: Returns "Generic"

---

### 7. Builder Pattern Class

#### `ArchiveNameBuilder`
- **Type**: Builder Class
- **Purpose**: Constructs complex archive filenames step-by-step with optional components
- **Responsibilities**:
  - Build custom archive names with prefix, timestamp, suffix
  - Support flexible component combination
  - Generate full file paths with directory
  - Allow builder reuse with reset functionality
- **Design Pattern**: **Builder Pattern**
- **Justification**:
  - Archive names have many optional components (prefix, timestamp, original name, suffix)
  - Provides fluent interface for readable code
  - Separates construction logic from representation
  - Easily extensible for new naming components
  - Avoids telescoping constructor anti-pattern
- **Configurable Components**:
  - Prefix (e.g., "backup", "archive")
  - Original filename (without extension)
  - Timestamp (customizable format, default: yyyyMMdd_HHmmss)
  - Suffix (e.g., "final", "v2")
  - Extension (e.g., ".zip", ".rar")
  - Separator (default: "_")
- **Example Usage**:
  ```java
  String archiveName = new ArchiveNameBuilder()
      .withPrefix("compressed_archive")
      .withTimestamp()
      .withExtension(".zip")
      .buildWithDirectory("C:\\Output");
  // Result: "C:\\Output\\compressed_archive_20231210_143000.zip"
  ```
- **Key Methods**:
  - `withPrefix(String)`: Set prefix
  - `withOriginalFileName(String)`: Include original filename
  - `withTimestamp()`: Add current timestamp
  - `withCustomTimestamp(String)`: Add custom format timestamp
  - `withSuffix(String)`: Set suffix
  - `withExtension(String)`: Set file extension
  - `withSeparator(String)`: Set component separator
  - `build()`: Build filename
  - `buildWithDirectory(String)`: Build full path
  - `reset()`: Reset to defaults for reuse

---

### 8. Prototype Pattern Class

#### `FileSelection`
- **Type**: Cloneable Data Class
- **Purpose**: Represents a file selection configuration that can be cloned
- **Responsibilities**:
  - Store selected files, compression type, output directory
  - Provide deep cloning of file selection
  - Manage file list operations (add, remove, clear)
- **Design Pattern**: **Prototype Pattern**
- **Justification**:
  - Users may want to save and duplicate file selections
  - Cloning is more efficient than rebuilding selections from scratch
  - Allows creating variations of existing selections
  - Useful for batch operations with similar configurations
- **Implementation Details**:
  - Implements `Cloneable` interface
  - Performs deep copy of file list
  - Automatically appends "(Copy)" to cloned selection names
- **Use Case Example**:
  1. User selects 10 files and configures compression settings
  2. User clones the selection
  3. User modifies the clone (e.g., adds/removes files)
  4. Both selections can be compressed independently
- **Stored Data**:
  - Selection name
  - List of selected files
  - Compression type (ZIP/RAR)
  - Output directory
- **Key Methods**:
  - `clone()`: Creates deep copy of selection
  - `setSelectedFiles(File[])`: Update file selection
  - `addFile(File)`: Add single file
  - `removeFile(File)`: Remove single file
  - `clearFiles()`: Clear all files
  - `getFilesAsArray()`: Get files as array
  - `getFilePathsAsArray()`: Get file paths as string array

---

### 9. Utility Classes

#### `FileHandler`
- **Type**: Singleton Utility Class
- **Purpose**: Centralized file I/O operations
- **Responsibilities**:
  - Read files into byte arrays
  - Write byte arrays to files
  - Check file existence
- **Design Pattern**: **Singleton Pattern** (Lazy Initialization)
- **Justification**:
  - Only one file handler instance needed
  - Provides centralized file access point
  - Could be extended with caching or logging in the future
  - Ensures consistent file handling across the application
- **Key Methods**:
  - `getInstance()`: Returns singleton instance
  - `readFile(String filePath)`: Reads file to byte array
  - `writeFile(String filePath, byte[] data)`: Writes byte array to file
  - `fileExists(String filePath)`: Checks if file exists

---

## Pattern Justification

### 1. Singleton Pattern

**Used in**: `CompressionManager`, `FileHandler`

**Why we use it**:
- **Single Point of Control**: Only one instance of these managers should exist to ensure consistent state and behavior
- **Global Access**: These services need to be accessed from multiple parts of the application
- **Resource Management**: Prevents multiple instances from managing the same resources
- **Lazy Initialization**: Instance created only when first needed, saving memory

**Benefits in this project**:
- `CompressionManager` ensures all compression operations go through a single, coordinated manager
- `FileHandler` provides centralized file I/O, making it easier to add logging or caching later
- Reduces memory footprint by having single instances
- Simplifies testing and debugging

**Trade-offs**:
- Can make unit testing slightly more difficult (mitigated by using getInstance() method)
- Not ideal for multi-threaded scenarios without synchronization (acceptable for this single-user GUI app)

---

### 2. Factory Pattern

**Used in**: `CompressionFactory`, `FileTypeFactory`

**Why we use it**:
- **Encapsulation of Creation Logic**: Hides complex instantiation logic from clients
- **Flexibility**: Easy to add new compression types or file processors without changing client code
- **Centralized Configuration**: All object creation logic in one place
- **Loose Coupling**: Clients depend on interfaces, not concrete classes

**Benefits in this project**:

**CompressionFactory**:
- Adding a new compression format (e.g., 7z, tar.gz) requires only:
  1. Create new `Compressor` implementation
  2. Add case to factory
  3. No changes to `FileSelectionPanel` or `CompressionManager`
- Encapsulates the decision logic for which compressor to use

**FileTypeFactory**:
- Easily extensible for new file types (e.g., audio files, PDFs)
- Provides intelligent defaults (DefaultProcessor) for unknown types
- Centralizes file extension mapping
- Separates type detection from processing logic

**Trade-offs**:
- Adds an extra layer of indirection
- Factory classes can become large if many types are supported (mitigated by good organization)

---

### 3. Adapter Pattern

**Used in**: `RarCompressorAdapter`

**Why we use it**:
- **Integration with External Tools**: Allows using WinRAR command-line tools without reimplementing RAR algorithm
- **Interface Compatibility**: Makes external CLI tools work with our `Compressor` interface
- **Reusability**: Allows leveraging existing, well-tested tools
- **Flexibility**: Can swap RAR implementation without changing client code

**Benefits in this project**:
- No need to implement complex RAR compression algorithm from scratch
- Uses industry-standard WinRAR tools (reliable and efficient)
- Maintains consistent interface across ZIP and RAR compression
- Easy to replace with alternative RAR implementation (e.g., Java library) if needed
- Client code (FileSelectionPanel) doesn't know or care that RAR uses external processes

**Adaptation Process**:
```
Client Code                Adapter                    External Tool
-----------                -------                    -------------
compress(path) ──────────> compress(path) ──────────> rar a -ep archive.rar file
                           [translates]
                           [executes process]
                           [reads output]
                           [returns boolean]
```

**Trade-offs**:
- Depends on external WinRAR installation
- Slightly slower due to process spawning
- Less control over error handling compared to native library

---

### 4. Builder Pattern

**Used in**: `ArchiveNameBuilder`

**Why we use it**:
- **Complex Object Construction**: Archive names have many optional components
- **Readability**: Fluent interface makes code self-documenting
- **Flexibility**: Easily combine or omit components
- **Avoid Telescoping Constructors**: No need for multiple constructors with different parameter combinations

**Benefits in this project**:
- Clear, readable code when building archive names:
  ```java
  // Without Builder (ugly)
  String name = createArchiveName("prefix", null, true, "suffix", ".zip", "_");
  
  // With Builder (clean and readable)
  String name = new ArchiveNameBuilder()
      .withPrefix("prefix")
      .withTimestamp()
      .withSuffix("suffix")
      .withExtension(".zip")
      .build();
  ```
- Easy to add new components (e.g., version number, user name) without breaking existing code
- Provides sensible defaults for all components
- Reusable with `reset()` method

**Use Cases in Project**:
- Multi-file compression: Generate unique archive names with timestamps
- User-defined naming schemes (could be extended with GUI options)
- Automated batch compression with consistent naming

**Trade-offs**:
- More code than simple constructor (justified by improved readability)
- Slight performance overhead (negligible for this use case)

---

### 5. Prototype Pattern

**Used in**: `FileSelection`

**Why we use it**:
- **Object Cloning**: Allows users to duplicate existing file selections
- **Efficiency**: Faster than recreating selections from scratch
- **Flexibility**: Easy to create variations of existing selections
- **State Preservation**: Maintains full state of original selection

**Benefits in this project**:
- User can save frequently used file selections
- Quick creation of similar compression jobs (e.g., same files, different format)
- Potential for "saved selections" feature in future versions
- Deep copying ensures independence of cloned objects

**Example Scenario**:
```java
// User selects 20 files for ZIP compression
FileSelection originalSelection = new FileSelection("My Documents");
originalSelection.setSelectedFiles(files);
originalSelection.setCompressionType("ZIP");

// User wants to also create a RAR archive of the same files
FileSelection rarSelection = originalSelection.clone();
rarSelection.setCompressionType("RAR");

// Both selections can now be processed independently
```

**Implementation Details**:
- Implements `Cloneable` interface
- Performs deep copy of file list (not just reference copy)
- Automatically renames clones with "(Copy)" suffix
- Prevents accidental modification of original selection

**Trade-offs**:
- Requires careful implementation to ensure proper deep copying
- Currently underutilized (could be expanded with save/load functionality)

---

### 6. Strategy Pattern (Interface-based Polymorphism)

**Used in**: `Compressor` interface, `FileTypeProcessor` interface

**Why we use it**:
- **Interchangeable Algorithms**: Different compression algorithms and file processors can be swapped at runtime
- **Open/Closed Principle**: Open for extension (new strategies) but closed for modification
- **Separation of Concerns**: Each strategy focuses on one algorithm/approach
- **Runtime Flexibility**: Compression type chosen by user at runtime

**Benefits in this project**:

**Compressor Interface**:
- User can switch between ZIP and RAR at runtime via dropdown
- `CompressionManager` works with any `Compressor` implementation
- Adding new compression formats doesn't require changing existing code
- Each compressor implementation is independent and testable

**FileTypeProcessor Interface**:
- Different processing strategies for different file types
- Automatically selects appropriate processor based on file extension
- Each processor encapsulates type-specific logic
- Easy to add new file type processors (e.g., AudioProcessor, PdfProcessor)

**Strategy Selection**:
```java
// Compression strategy selected by user
Compressor compressor = CompressionFactory.createCompressor(userChoice);
compressionManager.compressFile(file, compressor, progressBar);

// File type strategy selected automatically
FileTypeProcessor processor = FileTypeFactory.getProcessor(extension);
byte[] processedData = processor.prepareForCompression(rawData);
```

**Trade-offs**:
- Multiple classes needed (one per strategy)
- Client must be aware of different strategies (mitigated by factories)

---

## Summary of Pattern Benefits

| Pattern | Primary Benefit | Secondary Benefit | Extensibility Impact |
|---------|----------------|-------------------|---------------------|
| **Singleton** | Single instance control | Global access point | Low (by design) |
| **Factory** | Encapsulated creation | Loose coupling | **High** - Easy to add new types |
| **Adapter** | External integration | Interface compatibility | Medium - Swap implementations |
| **Builder** | Readable construction | Flexible configuration | **High** - Add new components |
| **Prototype** | Object cloning | State preservation | Medium - Clone variations |
| **Strategy** | Interchangeable algorithms | Runtime flexibility | **High** - Add new strategies |

---

## Future Enhancement Opportunities

The current design patterns enable easy future enhancements:

1. **New Compression Formats** (Factory Pattern):
   - Add 7z, tar.gz, bz2 support
   - Implement `Compressor` interface
   - Add case to `CompressionFactory`

2. **New File Type Processors** (Factory Pattern):
   - `AudioProcessor` for MP3, WAV, FLAC
   - `DocumentProcessor` for PDF, DOCX
   - `ArchiveProcessor` for nested archives

3. **Archive Name Templates** (Builder Pattern):
   - Add version numbers
   - Include file count
   - User-defined templates

4. **Saved selections** (Prototype Pattern):
   - Save/load selections to file
   - Selection library/history
   - Quick access to favorite selections

5. **Compression Strategies** (Strategy Pattern):
   - Different compression levels
   - Encryption options
   - Split archives

6. **Advanced Features**:
   - Batch processing multiple selections
   - Scheduled compression jobs
   - Cloud storage integration (new Adapter)
   - Compression comparison (size, speed)

---

## Conclusion

This File Compression Utility demonstrates professional software engineering practices through the strategic use of design patterns. Each pattern serves a specific purpose:

- **Singleton**: Ensures centralized, consistent management
- **Factory**: Enables extensibility and loose coupling
- **Adapter**: Integrates external tools seamlessly
- **Builder**: Provides flexible object construction
- **Prototype**: Supports efficient object duplication
- **Strategy**: Allows interchangeable algorithms

Together, these patterns create a robust, maintainable, and extensible application architecture that can easily accommodate future requirements and enhancements.

---

## Class Relationship Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                    File_Compression_Uillity                      │
│                          [Main Class]                            │
└────────────────────────────┬─────────────────────────────────────┘
                             │ creates
                             ↓
┌──────────────────────────────────────────────────────────────────┐
│                      FileSelectionPanel                          │
│                         [GUI Layer]                              │
│  - Uses: CompressionFactory, FileTypeFactory                     │
│  - Uses: ArchiveNameBuilder (Builder Pattern)                    │
│  - Uses: FileSelection (Prototype Pattern)                       │
└────────────────────┬────────────────────┬────────────────────────┘
                     │                    │
                     │ uses               │ uses
                     ↓                    ↓
      ┌──────────────────────┐  ┌──────────────────────┐
      │ CompressionManager   │  │    FileHandler       │
      │   [Singleton]        │  │   [Singleton]        │
      └──────────────────────┘  └──────────────────────┘
                     │
                     │ delegates to
                     ↓
      ┌──────────────────────────────────────┐
      │    Compressor Interface              │
      │      [Strategy Pattern]              │
      └────────┬────────────┬────────────────┘
               │            │
               ↓            ↓
    ┌──────────────┐  ┌──────────────────────┐
    │ZipCompressor │  │RarCompressorAdapter  │
    │              │  │  [Adapter Pattern]   │
    └──────────────┘  └──────────────────────┘
                               │
                               │ adapts
                               ↓
                      ┌──────────────────┐
                      │  WinRAR CLI      │
                      │  (External Tool) │
                      └──────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│              FileTypeFactory [Factory Pattern]                   │
└────────┬────────────┬────────────┬──────────────┬────────────────┘
         │            │            │              │
         ↓            ↓            ↓              ↓
  ┌─────────────┐ ┌─────────────┐ ┌─────────┐ ┌────────────────┐
  │TextProcessor│ │ImageProcessor│ │Video    │ │DefaultProcessor│
  │             │ │              │ │Processor│ │                │
  └─────────────┘ └──────────────┘ └─────────┘ └────────────────┘
         └──────────┬────────────────┴──────────────┘
                    │ implements
                    ↓
         ┌────────────────────────┐
         │ FileTypeProcessor      │
         │ [Strategy Interface]   │
         └────────────────────────┘
```

---

**Document Version**: 1.0  
**Last Updated**: December 15, 2025  
**Author**: File Compression Utility Development Team
