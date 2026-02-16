# Design Patterns Quick Reference

## Overview
This document provides a quick reference for all design patterns used in the File Compression Utility project.

---

## Pattern Summary Table

| # | Pattern Name | Classes | Purpose | Key Benefit |
|---|-------------|---------|---------|-------------|
| 1 | **Singleton** | `CompressionManager`<br>`FileHandler` | Single instance control | Centralized resource management |
| 2 | **Factory** | `CompressionFactory`<br>`FileTypeFactory` | Object creation | Easy extensibility for new types |
| 3 | **Adapter** | `RarCompressorAdapter` | External tool integration | Use WinRAR CLI with our interface |
| 4 | **Builder** | `ArchiveNameBuilder` | Complex object construction | Readable, flexible filename creation |
| 5 | **Prototype** | `FileSelection` | Object cloning | Duplicate file selections efficiently |
| 6 | **Strategy** | `Compressor` interface<br>`FileTypeProcessor` interface | Interchangeable algorithms | Runtime algorithm selection |

---

## Pattern Details

### 1. Singleton Pattern ⭐

**Intent**: Ensure a class has only one instance and provide global access to it.

**Implementation**:
```java
public class CompressionManager {
    private static CompressionManager instance;
    
    private CompressionManager() { } // Private constructor
    
    public static CompressionManager getInstance() {
        if (instance == null) {
            instance = new CompressionManager();
        }
        return instance;
    }
}
```

**Used In**:
- ✅ `CompressionManager` - Manages all compression operations
- ✅ `FileHandler` - Handles all file I/O operations

**Why?**
- Only one compression manager needed
- Centralized control
- Global access point
- Lazy initialization saves resources

---

### 2. Factory Pattern 🏭

**Intent**: Define an interface for creating objects, but let subclasses decide which class to instantiate.

**Implementation**:
```java
public class CompressionFactory {
    public static Compressor createCompressor(String type) {
        switch (type.toUpperCase()) {
            case "ZIP":
                return new ZipCompressor();
            case "RAR":
                return new RarCompressorAdapter();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }
}
```

**Used In**:
- ✅ `CompressionFactory` - Creates `Compressor` instances (ZIP, RAR)
- ✅ `FileTypeFactory` - Creates `FileTypeProcessor` instances (Text, Image, Video, Default)

**Why?**
- **Encapsulation**: Hides object creation complexity
- **Extensibility**: Add new types without changing client code
- **Loose Coupling**: Clients depend on interfaces, not concrete classes

**Example**:
```java
// Easy to add new compression types
case "7Z":
    return new SevenZipCompressor(); // Just add this!
```

---

### 3. Adapter Pattern 🔌

**Intent**: Convert the interface of a class into another interface clients expect.

**Implementation**:
```java
public class RarCompressorAdapter implements Compressor {
    @Override
    public boolean compress(String filePath) {
        // Adapt: Java method call → Command-line execution
        ProcessBuilder pb = new ProcessBuilder(
            rarExecutablePath, "a", "-ep", outputPath, filePath
        );
        Process process = pb.start();
        return process.waitFor() == 0;
    }
}
```

**Used In**:
- ✅ `RarCompressorAdapter` - Adapts WinRAR command-line tools to `Compressor` interface

**Adaptation**:
```
Compressor Interface  →  Adapter  →  WinRAR CLI
(compress method)        (translates)  (rar a -ep...)
```

**Why?**
- Use external WinRAR tools without reimplementing RAR algorithm
- Maintain consistent interface for all compression types
- Easy to swap RAR implementation in the future

---

### 4. Builder Pattern 🔨

**Intent**: Separate the construction of a complex object from its representation.

**Implementation**:
```java
String archiveName = new ArchiveNameBuilder()
    .withPrefix("backup")
    .withTimestamp()
    .withExtension(".zip")
    .buildWithDirectory("C:\\Output");
// Result: "C:\\Output\\backup_20231210_143000.zip"
```

**Used In**:
- ✅ `ArchiveNameBuilder` - Builds complex archive filenames step-by-step

**Components**:
- Prefix (e.g., "backup")
- Original filename
- Timestamp (customizable format)
- Suffix (e.g., "final")
- Extension (.zip, .rar)
- Separator (default: "_")

**Why?**
- **Readability**: Fluent interface is self-documenting
- **Flexibility**: Easily combine or omit components
- **Maintainability**: Add new components without breaking existing code
- **Avoids**: Telescoping constructor anti-pattern

---

### 5. Prototype Pattern 📋

**Intent**: Create new objects by copying existing instances.

**Implementation**:
```java
public class FileSelection implements Cloneable {
    @Override
    public FileSelection clone() {
        FileSelection cloned = (FileSelection) super.clone();
        // Deep copy file list
        cloned.selectedFiles = new ArrayList<>();
        for (File file : this.selectedFiles) {
            cloned.selectedFiles.add(new File(file.getAbsolutePath()));
        }
        cloned.selectionName = this.selectionName + " (Copy)";
        return cloned;
    }
}
```

**Used In**:
- ✅ `FileSelection` - Stores file selection and allows cloning

**Why?**
- Users can duplicate file selections
- Faster than rebuilding from scratch
- Create variations of existing selections
- Useful for batch operations

**Use Case**:
```java
// Create original selection
FileSelection zipSelection = new FileSelection("My Files");
zipSelection.setCompressionType("ZIP");

// Clone for RAR version
FileSelection rarSelection = zipSelection.clone();
rarSelection.setCompressionType("RAR");

// Both can be compressed independently
```

---

### 6. Strategy Pattern 🎯

**Intent**: Define a family of algorithms, encapsulate each one, and make them interchangeable.

**Implementation**:

**Strategy Interface**:
```java
public interface Compressor {
    boolean compress(String filePath);
    boolean decompress(String filePath);
    boolean compressMultiple(String[] filePaths, String output);
    String getExtension();
}
```

**Concrete Strategies**:
- `ZipCompressor` - ZIP compression algorithm
- `RarCompressorAdapter` - RAR compression algorithm

**Used In**:
- ✅ `Compressor` interface - Different compression algorithms
- ✅ `FileTypeProcessor` interface - Different file type processors

**Why?**
- Interchangeable algorithms at runtime
- User selects compression type via dropdown
- Easy to add new compression strategies
- Each strategy is independent and testable

**Strategy Selection**:
```java
// Selected by user at runtime
String userChoice = TypeComboBox.getSelectedItem();
Compressor compressor = CompressionFactory.createCompressor(userChoice);
compressionManager.compressFile(file, compressor, progressBar);
```

---

## Pattern Interactions

### How Patterns Work Together

```
User selects "ZIP" and clicks "Compress"
          ↓
FileSelectionPanel (GUI)
          ↓
1. [Prototype] Stores selection in FileSelection object
          ↓
2. [Singleton] Gets CompressionManager instance
          ↓
3. [Factory] CompressionFactory.createCompressor("ZIP")
          ↓
4. [Strategy] Returns ZipCompressor implementing Compressor
          ↓
5. [Factory] FileTypeFactory.getProcessor(extension)
          ↓
6. [Strategy] Returns appropriate FileTypeProcessor
          ↓
7. [Builder] ArchiveNameBuilder builds output filename
          ↓
8. [Singleton] CompressionManager.compressFile(...)
          ↓
9. Compression completed!
```

---

## When to Add New Features

### Adding a New Compression Format (e.g., 7z)

1. **Create Strategy**: Implement `Compressor` interface
   ```java
   public class SevenZipCompressor implements Compressor {
       // Implementation
   }
   ```

2. **Update Factory**: Add to `CompressionFactory`
   ```java
   case "7Z":
       return new SevenZipCompressor();
   ```

3. **Update GUI**: Add "7Z" to dropdown
   ```java
   TypeComboBox.setModel(new String[] { "ZIP", "RAR", "7Z" });
   ```

**That's it!** No other changes needed. ✅

---

### Adding a New File Type Processor (e.g., Audio)

1. **Create Strategy**: Implement `FileTypeProcessor` interface
   ```java
   public class AudioProcessor implements FileTypeProcessor {
       // Implementation
   }
   ```

2. **Update Factory**: Add to `FileTypeFactory`
   ```java
   case "mp3":
   case "wav":
   case "flac":
       return new AudioProcessor();
   ```

**That's it!** Works automatically. ✅

---

## Benefits Summary

### Maintainability 🛠️
- Clear separation of concerns
- Each class has single responsibility
- Easy to locate and fix bugs

### Extensibility 📈
- Add new compression formats without touching existing code
- Add new file type processors independently
- Extend archive naming without breaking existing functionality

### Testability ✅
- Each strategy testable in isolation
- Mock factories for unit tests
- Singletons can be reset for testing

### Reusability ♻️
- Processors reusable across different contexts
- Builders reusable with reset()
- Prototypes allow efficient duplication

---

## Design Principles Followed

✅ **SOLID Principles**:
- **S**ingle Responsibility: Each class has one job
- **O**pen/Closed: Open for extension, closed for modification
- **L**iskov Substitution: Strategies are interchangeable
- **I**nterface Segregation: Focused interfaces
- **D**ependency Inversion: Depend on abstractions, not concretions

✅ **DRY** (Don't Repeat Yourself): Factories eliminate duplicate creation code

✅ **Separation of Concerns**: Clear layer boundaries

---

## Pattern Selection Guide

**When to use each pattern:**

| Scenario | Pattern | Reason |
|----------|---------|--------|
| Need only one instance | Singleton | Control instantiation |
| Many object variations | Factory | Centralize creation |
| Integrate external system | Adapter | Interface compatibility |
| Complex object construction | Builder | Readability & flexibility |
| Need to copy objects | Prototype | Efficient duplication |
| Interchangeable algorithms | Strategy | Runtime selection |

---

## Conclusion

This project demonstrates **professional software architecture** through strategic use of design patterns. Each pattern serves a specific purpose and contributes to an:

- **Extensible** system (easy to add features)
- **Maintainable** codebase (easy to modify)
- **Testable** application (easy to verify)
- **Understandable** structure (easy to learn)

The patterns work together harmoniously, creating a robust foundation for current and future requirements.

---

**For detailed information**, see: `PROJECT_DOCUMENTATION.md`
