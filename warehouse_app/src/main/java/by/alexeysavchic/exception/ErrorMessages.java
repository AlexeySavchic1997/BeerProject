package by.alexeysavchic.exception;

public class ErrorMessages {
    public final static String xmlReadingException = "warehouse service exception: unable to read .xml file";

    public final static String xmlWritingException = "warehouse service exception: unable to write .xml file";

    public final static String invalidXmlException = "warehouse service exception: received pattern mismatch .xml file";

    public final static String fileNotFoundException = "warehouse service exception: file %s did not found";

    public final static String MalformedXmlFileException = "warehouse service exception: file %s is malformed";

    public final static String ChangingXmlDuringExecuting = "warehouse service exception: file %s was modified during execution";

    public final static String CannotRewriteFile = "warehouse service exception: Cannot rewrite %s file";

}
