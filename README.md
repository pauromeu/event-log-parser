
# Event Log Parser

## Overview
Parses a JSON event log file, pairs STARTED and FINISHED events by ID, calculates durations, flags alerts for duration > 4ms, and stores results in an embedded HSQLDB.

## Features
- Reads logfile.txt with one JSON event per line
- Matches STARTED and FINISHED events, calculates duration
- Flags alerts for durations > 4ms
- Stores processed events in local file-based HSQLDB
- Includes unit tests for parsing, pairing, and DB operations

## Requirements
- Java 11 (OpenJDK)
- Gradle 8.14

## Setup & Build
1. Clone repo
2. Ensure Java 11 and Gradle 8.14 installed
3. Run: ./gradlew build

## Running
Place logfile.txt anywhere, then run:
./gradlew :app:run --args="/full/path/to/logfile.txt"
If you keep the example file in the project root, run:
```bash
./gradlew :app:run --args="$(pwd)/logfile.txt"
```
## Database
DB files stored in project-root/data/ folder (gitignored)
Stores ID, duration, type, host, alert flag

## Testing
Run tests with:
```bash
./gradlew :app:test
```

## Logs
- The program uses logback for logging.
- Logs are printed to the console and saved to logs/app.log.

## Notes
- Skips invalid JSON lines
- One STARTED and FINISHED per ID expected
- Duration is absolute difference of timestamps
- Alerts for duration > 4ms
- DB upsert done as delete + insert for simplicity

## Next Steps
- Expand unit tests to cover edge cases, error handling, and database interactions.
- Improve processing efficiency by using buffered streaming and batch database inserts.
- Enable handling of very large files (gigabytes) by implementing streaming parsers and incremental processing to avoid loading entire files into memory.

## Contact
Pau Romeu Llordella - [romeu.pau@gmail.com]

