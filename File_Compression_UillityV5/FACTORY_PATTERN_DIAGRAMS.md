# File Type Factory Pattern - Architecture Diagram

## Class Hierarchy

```
┌─────────────────────────────────────────────────────┐
│              <<interface>>                          │
│           FileTypeProcessor                         │
│─────────────────────────────────────────────────────│
│ + prepareForCompression(byte[]): byte[]             │
│ + getFileType(): String                             │
│ + getProcessingDescription(): String                │
└─────────────────────────────────────────────────────┘
                        ▲
                        │ implements
         ┌──────────────┼──────────────┬──────────────┐
         │              │              │              │
┌────────┴────────┐ ┌──┴──────────┐ ┌─┴────────────┐ ┌┴──────────────┐
│ TextProcessor   │ │ImageProcessor│ │VideoProcessor│ │DefaultProcessor│
├─────────────────┤ ├──────────────┤ ├──────────────┤ ├───────────────┤
│ Text files:     │ │ Image files: │ │ Video files: │ │ Unknown files:│
│ - Normalize \n  │ │ - Get dims   │ │ - Detect fmt │ │ - No process  │
│ - Trim spaces   │ │ - Validate   │ │ - Validate   │ │ - Pass through│
│ - Reduce blanks │ │ - Log info   │ │ - Log info   │ │ - Log size    │
└─────────────────┘ └──────────────┘ └──────────────┘ └───────────────┘

┌─────────────────────────────────────────────────────┐
│          FileTypeFactory                            │
│             (Factory)                               │
│─────────────────────────────────────────────────────│
│ + getProcessor(String): FileTypeProcessor           │
│ + getFileExtension(String): String                  │
│─────────────────────────────────────────────────────│
│ Creates appropriate processor based on extension:   │
│ • txt, json, java → TextProcessor                   │
│ • jpg, png, gif   → ImageProcessor                  │
│ • mp4, avi, mkv   → VideoProcessor                  │
│ • unknown         → DefaultProcessor                │
└─────────────────────────────────────────────────────┘
```

## Workflow Diagram

```
User Selects File
       │
       ▼
┌──────────────────┐
│ Extract extension│ ← FileTypeFactory.getFileExtension()
│ (e.g., "txt")    │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Factory creates  │ ← FileTypeFactory.getProcessor("txt")
│ TextProcessor    │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Read file data   │ ← FileHandler.readFile()
│ (byte[])         │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Process data     │ ← processor.prepareForCompression()
│ (optimize)       │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Compress file    │ ← ZipCompressor / RarCompressorAdapter
│                  │
└──────────────────┘
```

## File Extension Mapping

```
TEXT FILES                  IMAGE FILES               VIDEO FILES
──────────                  ───────────               ───────────
.txt  ─┐                   .jpg  ─┐                  .mp4  ─┐
.log  ─┤                   .jpeg ─┤                  .avi  ─┤
.md   ─┤                   .png  ─┤                  .mkv  ─┤
.xml  ─┤                   .gif  ─┤                  .mov  ─┤
.json ─┼─→ TextProcessor   .bmp  ─┼─→ ImageProcessor .wmv  ─┼─→ VideoProcessor
.csv  ─┤                   .tiff ─┤                  .flv  ─┤
.html ─┤                   .webp ─┘                  .webm ─┘
.java ─┤
.py   ─┤
.cpp  ─┘

UNKNOWN FILES
─────────────
.xyz  ─┐
.dat  ─┼─→ DefaultProcessor
.bin  ─┘
```

## Processing Comparison

```
┌──────────────┬─────────────────────────────────────────────────────┐
│ File Type    │ Processing Action                                   │
├──────────────┼─────────────────────────────────────────────────────┤
│ Text         │ ✓ Normalizes line endings (\r\n → \n)               │
│              │ ✓ Removes trailing whitespace                       │
│              │ ✓ Reduces consecutive blank lines                   │
│              │ → Better compression ratio                          │
├──────────────┼─────────────────────────────────────────────────────┤
│ Image        │ ✓ Extracts dimensions (width × height)              │
│              │ ✓ Validates image format                            │
│              │ ✓ Logs metadata to console                          │
│              │ → No data modification (already optimized)          │
├──────────────┼─────────────────────────────────────────────────────┤
│ Video        │ ✓ Detects format (MP4/AVI/MKV)                      │
│              │ ✓ Validates file signature                          │
│              │ ✓ Logs file information                             │
│              │ → No data modification (already compressed)         │
├──────────────┼─────────────────────────────────────────────────────┤
│ Generic      │ ✓ Logs file size                                    │
│              │ → Direct pass-through, no processing                │
└──────────────┴─────────────────────────────────────────────────────┘
```

## Benefits Visualization

```
┌────────────────────────────────────────────────────────────┐
│                    FACTORY PATTERN BENEFITS                │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  1. EXTENSIBILITY                                          │
│     ┌─────────┐                                            │
│     │ Add new │ → Simply create new processor class        │
│     │  type   │    No changes to existing code             │
│     └─────────┘                                            │
│                                                            │
│  2. MAINTAINABILITY                                        │
│     ┌─────────┐                                            │
│     │ Fix bug │ → Only modify specific processor           │
│     │ in text │    Other processors unaffected             │
│     └─────────┘                                            │
│                                                            │
│  3. FLEXIBILITY                                            │
│     ┌─────────┐                                            │
│     │Different│ → Each type has custom logic               │
│     │strategy │    Optimized for specific needs            │
│     └─────────┘                                            │
│                                                            │
│  4. TESTABILITY                                            │
│     ┌─────────┐                                            │
│     │ Test in │ → Mock processors for unit tests           │
│     │isolation│    Test factory logic separately           │
│     └─────────┘                                            │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

## Real Example Output

When compressing `example.txt`:

```
=== FILE TYPE PROCESSING ===
File: example.txt
Extension: txt
Processor Type: Text
Processing: Normalizes line endings, removes trailing whitespace, and reduces blank lines
============================

TextProcessor: Processing text file...
Original size: 1024 bytes
Processed size: 987 bytes (3.6% reduction)

Compressing Text file...
Compression completed successfully!
```
