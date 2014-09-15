package org.wso2.carbon.utility.jenkins.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileUtils is a class to create temporary xml file
 *
 * @author Pubudu Dissanayake : pubudud@wso2.com on 02/09/2014.
 */
public class FileUtils {
	/**
	 * get xml file from input stream
	 *
	 * @param inputStream
	 * @param prefix
	 * @param suffix
	 * @return a temporary xml file
	 * @throws IOException
	 */
	public static File getFileFromInputStream(InputStream inputStream, String prefix, String suffix) throws IOException {
		File tempXmlFile = null;
		OutputStream outputStream = null;
		try {
			tempXmlFile = File.createTempFile(prefix, suffix);
			tempXmlFile.deleteOnExit();
			outputStream = new FileOutputStream(tempXmlFile);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			throw new IOException();
		} finally {
			if (inputStream != null && outputStream != null) {
				inputStream.close();
				outputStream.close();
			}
			return tempXmlFile;
		}
	}
}
