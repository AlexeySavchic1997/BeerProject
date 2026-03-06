package by.alexeysavchic.configuration;

import by.alexeysavchic.exception.FileDidNotFoundException;
import by.alexeysavchic.exception.MalformedXmlFileException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class StreamContext implements Closeable {
    private static final Logger logger = LogManager.getLogger(StreamContext.class);

    private File xmlPath;

    private File intermediateXml;

    private FileReader fileReader;

    private FileWriter fileWriter;

    private XMLStreamReader streamReader;

    private XMLStreamWriter streamWriter;

    private final XMLInputFactory xmlInputFactory;

    private final XMLOutputFactory xmlOutputFactory;

    public StreamContext(File xmlPath, File intermediateXml, XMLInputFactory xmlInputFactory, XMLOutputFactory xmlOutputFactory) {
        this.xmlPath = xmlPath;
        this.intermediateXml = intermediateXml;
        this.xmlInputFactory = xmlInputFactory;
        this.xmlOutputFactory = xmlOutputFactory;
    }

    public XMLStreamReader newXmlStreamReader() {
        try {
            fileReader = new FileReader(xmlPath);
            streamReader = xmlInputFactory.createXMLStreamReader(fileReader);
            return streamReader;
        } catch (FileNotFoundException e) {
            throw new FileDidNotFoundException(xmlPath.toString());
        } catch (XMLStreamException e) {
            throw new MalformedXmlFileException(xmlPath.toString());
        }

    }

    public XMLStreamWriter newXmlStreamWriter() {
        try {
            fileWriter = new FileWriter(intermediateXml);
            streamWriter = xmlOutputFactory.createXMLStreamWriter(fileWriter);
            return streamWriter;
        } catch (IOException e) {
            throw new FileDidNotFoundException(xmlPath.toString());
        } catch (XMLStreamException e) {
            throw new MalformedXmlFileException(xmlPath.toString());
        }

    }

    private void closeStreamWriter(XMLStreamWriter resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (XMLStreamException e) {
                logger.error("Cannot close XmlStreamWriter", e);
            }
        }
    }

    private void closeFileWriter(FileWriter resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                logger.error("Cannot close FileWriter", e);
            }
        }
    }

    private void closeStreamReader(XMLStreamReader resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (XMLStreamException e) {
                logger.error("Cannot close XmlStreamReader", e);
            }
        }
    }

    private void closeFileReader(FileReader resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                logger.error("Cannot close FileReader", e);
            }
        }
    }

    @Override
    public void close() {
        if (streamWriter != null) {
            try {
                streamWriter.flush();
            } catch (XMLStreamException e) {
                logger.error("cannot clean stream buffer");
            }
        }
        closeStreamWriter(streamWriter);
        closeFileWriter(fileWriter);
        closeStreamReader(streamReader);
        closeFileReader(fileReader);
    }
}
