package org.wso2.carbon.utility.jenkins.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * FileUtils is a class to create temporary xml file
 * @author Pubudu Dissanayake : pubudud@wso2.com on 02/09/2014.
 */
public class FileUtils {
    /**
     * get xml file from input stream
     * @param inputStream
     * @param prefix
     * @param suffix
     * @return a temporary xml file
     * @throws IOException
     */
    public static File getFileFromInputStream(InputStream inputStream, String prefix, String suffix) throws IOException {
        File tempXmlFile = File.createTempFile(prefix, suffix);
        tempXmlFile.deleteOnExit();
        OutputStream outputStream = new FileOutputStream(tempXmlFile);
        IOUtils.copy(inputStream, outputStream);
        return tempXmlFile;
    }
}
