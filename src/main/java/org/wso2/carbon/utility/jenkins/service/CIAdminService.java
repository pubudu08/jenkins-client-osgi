package org.wso2.carbon.utility.jenkins.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utility.continuousintegration.ContinuousIntegrationArtifact;
import org.wso2.carbon.utility.continuousintegration.exception.GenericArtifactException;
import org.wso2.carbon.utility.jenkins.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CIAdminService is a class for jenkins APIs
 * this class will expose jenkins related functions as a service
 *
 * @author Pubudu Dissanayake : pubudud@wso2.com on 02/07/2014.
 */
public class CIAdminService implements ContinuousIntegrationArtifact {
	private static final String PREFIX = "TempXmlFile";
	private static final String SUFFIX = ".xml";
	private static final Log LOGGER = LogFactory.getLog(CIAdminService.class);

	/**
	 * This method will returns artifact type
	 *
	 * @return <code>Jenkins</code>
	 */
	@Override
	public String getCISType() {
		return "Jenkins";
	}

	/*
	This method will create CI Project. Based on project.xml template
	 */
	@Override
	public void createCISProject(String username, String password, String jenkinsEndPoint,
	                             String projectName) throws GenericArtifactException {
		InputStream inputStream = null;
		File xmlProjectFile = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = CIAdminService.class.getResourceAsStream("/project.xml");
			xmlProjectFile = FileUtils.getFileFromInputStream(inputStream, PREFIX, SUFFIX);
			JenkinsHttpClient jenkinsHttpClient =
			  new JenkinsHttpClient(new URI(jenkinsEndPoint), username, password);
			JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);

			Map<String, Job> mapJobList = jenkinsServer.getJobs();
			List<Job> jobList = new ArrayList<Job>(mapJobList.values());
			for (Job job : jobList) {
				if (projectName.equals(job.getName())) {
					throw new GenericArtifactException(
					  "Project is already exists, Please provide" +
					  "a suitable project name. ", "Project_Already_Exists");
				}
			}
			String line;
			bufferedReader = new BufferedReader(new FileReader(xmlProjectFile));
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line.trim());
			}
			jenkinsServer.createJob(projectName, stringBuilder.toString());
		} catch (IOException exception) {
			throw new GenericArtifactException("Unable to read project.xml",
			  exception, "XML_Read_Error");
		} catch (URISyntaxException exception) {
			throw new GenericArtifactException("Invalid server URL," +
			                                   " Please provide a valid server address.",
			  exception, "URISyntaxException");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException exception) {
					LOGGER.error("Failed to close the FileInputStream, file : " +
					             xmlProjectFile, exception);
				}
			} else if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException exception) {
					LOGGER.error("Failed to close the BufferedReader, file : " +
					             xmlProjectFile, exception);
				}
			}

		}
	}
}
