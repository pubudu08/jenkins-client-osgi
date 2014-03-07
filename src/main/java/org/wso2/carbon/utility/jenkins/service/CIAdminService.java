package org.wso2.carbon.utility.jenkins.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utility.continuousintegration.IContinuousIntegration;
import org.wso2.carbon.utility.jenkins.utils.FileUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CIAdminService is a class for jenkins APIs
 * this class will expose jenkins related functions as a service
 * @author Pubudu Dissanayake : pubudud@wso2.com on 02/07/2014.
 */
public class CIAdminService implements IContinuousIntegration {
    private static final String PREFIX = "TempXmlFile";
    private static final String SUFFIX = ".xml";
    private static final Log LOGGER = LogFactory.getLog(CIAdminService.class);

    /**
     * This method will returns artifact type
     * @return <code>Jenkins</code>
     */
    @Override
    public String getCISType() {
        return "Jenkins";
    }


    /**
     * This method will create a project which is related to iss
     * @param username valid username to access api
     * @param password  valid password to access api
     * @param projectKey  shorten name of the project
     * @param name     name of the project
     * @param description   description fo the project
     * @param url   optional field : provide url for the project
     * @param lead   required field : lead of the project
     * @return  <code>true</code> if project get successfully created
     */
    @Override
    public boolean createCISProject(String username, String password, String jenkinsEndPoint, String projectName) {
        InputStream inputStream = CIAdminService.class.getResourceAsStream("/project.xml");
        try {
            File xmlProjectFile = FileUtils.getFileFromInputStream(inputStream, PREFIX, SUFFIX);
            JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsEndPoint), username, password);
            JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
            String line = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(xmlProjectFile));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line.trim());
            }
            jenkinsServer.createJob(projectName, stringBuilder.toString());
            Map<String, Job> mapJobList = jenkinsServer.getJobs();
            List<Job> jobList = new ArrayList<Job>(mapJobList.values());
            for (Job job : jobList) {
                if (projectName.equals(job.getName())) {
                    LOGGER.info(" " + projectName + " jenkins project has been successfully created ");
                    return true;
                }
            }
            return false;

        } catch (IOException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;

        }
    }

    /**
     * check whether project is exists or not
     * @param username  valid username to access api
     * @param password  valid password to access api
     * @param projectKey shorten name of the project
     * @param url  endpoint url for the api
     * @return  <code>true</code> if project already exists
     */
    @Override
    public boolean isCISProjectExist(String username, String password, String jenkinsEndPoint,String projectName) {
        JenkinsHttpClient jenkinsHttpClient = null;
        try {
            jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsEndPoint), username, password);
            JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
            Map<String, Job> mapJobList = jenkinsServer.getJobs();
            List<Job> jobList = new ArrayList<Job>(mapJobList.values());
            for (Job job : jobList) {
                if (projectName.equals(job.getName())) {
                    return true;
                }
            }
            return false;
        } catch (URISyntaxException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
