package org.wso2.carbon.utility.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
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
 * @author Pubudu Dissanayake : pubudud@wso2.com on 2/7/14.
 */
public class Sample {
	private static final String PREFIX = "TempXmlFile";
	private static final String SUFFIX = ".xml";


	public static void main(String[] args) throws IOException {

		InputStream inputStream = Sample.class.getResourceAsStream("/project.xml");
		File xmlProjectFile = FileUtils.getFileFromInputStream(inputStream, PREFIX, SUFFIX);

		try {
			JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI("http://localhost:8080/"), "test", "test");
			JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
			Map<String, Job> jobList = jenkinsServer.getJobs();
			System.out.println(jobList);
			String line = "";
			BufferedReader bufferedReader = new BufferedReader(new FileReader(xmlProjectFile));
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line.trim());
			}
			System.out.println(stringBuilder);
			jobList = jenkinsServer.getJobs();

			List<Job> l = new ArrayList<Job>(jobList.values());

			for (Job job : l) {

				if ("test".equals(job.getName())) {
					System.out.println("Hurray");
				}
			}
			System.out.println(jobList);


		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}


}
