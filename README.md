# ScalaPixel

A simple image editor written in the Scala programming language. Built using [scala-swing](https://github.com/scala/scala-swing).

### Features

- Basic drawing tools and color selection for quick sketches and edits.
- Support for common operations like filters and flood fill.
- Intuitive UI with key shortcuts.
- Currently supported formats: jpg, png, bmp

## Demo

## Installation

ScalaPixel is a standalone application packaged as a single JAR file, making it easy to run on any platform with Java installed. Follow the steps below to download and run ScalaPixel on your system.

### Prerequisites

Ensure you have Java Runtime Environment (JRE) version 17.0.4 or newer installed on your system. You can download the latest version of Java from [AdoptOpenJDK](https://adoptopenjdk.net/).

### Download

1. Navigate to the [Releases](https://github.com/TheodorFransson/ScalaPixel/releases) page of the ScalaPixel GitHub repository.
2. Download the latest `ScalaPixel-<version>.jar` file from the assets.

#### Windows / Mac / Linux

1. Open Terminal (Command Prompt or similar on Windows).
2. Navigate to the folder containing `ScalaPixel-<version>.jar`.
3. Run the following command:

```
java -jar ScalaPixel-<version>.jar
```

Alternatively, if your system is configured to allow it, you may be able to run ScalaPixel by double-clicking the `ScalaPixel-<version>.jar` file. 

#### Platform Compatability Note

ScalaPixel has yet to be tested extensively on Mac and Linux, beware of minor visual and or functional issues.

## Roadmap

- [ ] Extensive testing on all platforms
- [ ] Layer capabilities
- [ ] Select tools
- [ ] Color history
- [ ] Inspectable history
- [ ] Greater image format support

## Acknowledgments

ScalaPixel incorporates the following third-party resources to enhance its user interface and functionality:

- **FlatLaf**: A modern open-source cross-platform Look and Feel for Java Swing applications. [FlatLaf GitHub Repository](https://github.com/JFormDesigner/FlatLaf)
- **Fluent UI System Icons**: A collection of familiar, friendly, and modern icons from Microsoft's Fluent UI design system. [Fluent UI Icons](https://github.com/microsoft/fluentui-system-icons)

## License
Copyright © 2024 Theodor Fransson.\
Distributed under the 
[MIT](https://choosealicense.com/licenses/mit/) License.
