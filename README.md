# ZEDEM Audio Downloader

Welcome to the ZEDEM Audio Downloader project! This Java FX application enables citizens from planet ZEDEM to download their favorite audio files from a central server using the ZEDEM protocol.

## Project Overview

The project consists of a client-server architecture where the server handles multiple client connections. Clients can log in, request a playlist of available audio files, download specific files, and log off.

## Project Structure

- **bin:** Empty directory for runnable binaries.
- **docs:** Contains .bat files for compiling the project without an IDE.
- **src:** Contains all relevant source code.
- **data:** Contains sub-folders for client and server where transferred files are saved.

## How to Run the Application

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/ReageNkoana/Client-Server-Audio-Downloader.git
    cd Client-Server-Audio-Downloader
    ```

2. **Compile and Run the Project:**
    - Use an IDE like Eclipse or follow the .bat files in the `docs` folder to compile and run the project.

3. **Execute the Server:**
    - Open the `Server.java` class in the `csc2b.server` package.
    - Run the `Server` class.

4. **Execute the Client:**
    - Open the `Client.java` class in the `csc2b.client` package.
    - Run the `Client` class.

5. **GUI Controls:**
    - Use the JavaFX GUI to interact with the ZEDEM server.
    - Buttons are provided for each ZEDEM command (BONJOUR, PLAYLIST, ZEDEMGET, ZEDEMBYE).

6. **File Storage:**
    - Transferred audio files are saved in the `data` folder.

## Dependencies

- JavaFX library
- Eclipse IDE (for development)

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- [JavaFX](https://openjfx.io/)
- [Eclipse IDE](https://www.eclipse.org/ide/)
