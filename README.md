# Distributed Systems

## Features

- Implementation of the `DAS` class (Distributed Application System).
- Logging and event recording using the `Log` class.
- Scripts to facilitate compilation and testing of the application.

## Requirements

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) version 8 or later.
- Windows operating system (the `.bat` scripts are prepared for this OS).

## Project Structure

```
Distributed Systems Task
├── .idea/                # IntelliJ IDEA configuration files
├── out/                  # Folder with compiled files
│   └── production/       # Compiled project classes
├── src/                  # Source code and scripts
│   ├── DAS.java          # Main project class
│   ├── Log.java          # Class for logging events
│   ├── compile.bat       # Compilation script
│   ├── test1.bat         # Test script 1
│   └── test2.bat         # Test script 2
└── .gitignore            # File to ignore temporary files in version control
```

## Instructions for Running

1. **Compile the Project**
   
   In the `src` folder, there is a `compile.bat` script. Run it to compile the `.java` files:
   ```
   cd src
   compile.bat
   ```

2. **Running Tests**
   
   The `test1.bat` and `test2.bat` scripts allow testing the project's functionalities:
   ```
   test1.bat
   test2.bat
   ```

3. **Running the Application**
   If the main class contains a `main` method, the application can be run using the command:
   ```
   java DAS
   ```

## Technologies Used

- **Java**: The main programming language used for implementing the system logic.
- **Windows Batch Scripts**: Supporting scripts to automate compilation and testing processes.

## Author

bdnn

## Notes

It is recommended to run the project in IntelliJ IDEA or another compatible IDE for easier code analysis and development.
