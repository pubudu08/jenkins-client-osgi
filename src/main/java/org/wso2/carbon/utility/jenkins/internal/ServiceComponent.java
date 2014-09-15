package org.wso2.carbon.utility.jenkins.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.utility.continuousintegration.ContinuousIntegrationArtifact;
import org.wso2.carbon.utility.jenkins.service.CIAdminService;

/**
 * @author Pubudu Dissanayake : pubudud@wso2.com on 2/7/14.
 * @scr.component name="org.wso2.carbon.utility.jenkins"
 * immediate="true"
 */

/**
 * ServiceComponent will set the necessary parameters that initiate Continuous Integration OSGi bundle
 */
public class ServiceComponent {
	private static final Log logger = LogFactory.getLog(ServiceComponent.class);
	private static CIAdminService adminService;
	private static BundleContext bundleContext;
	private ServiceRegistration registration;

	/**
	 * activate will set the necessary parameters that need to activate Continuous Integration OSGi bundle
	 *
	 * @param context Component Context
	 */
	protected void activate(ComponentContext context) {
		logger.info("Continuous Integration Service: Jenkins bundle is activated");
		adminService = new CIAdminService();
		bundleContext = context.getBundleContext();
		registration = bundleContext.registerService(ContinuousIntegrationArtifact.class.getName(),
		  adminService, null);
	}

	/**
	 * This will deactivate org.wso2.carbon.utility.jenkins bundle
	 *
	 * @param context
	 */
	protected void deactivate(ComponentContext context) {
		logger.info("Continuous Integration Service: Jenkins bundle is deactivated");
		registration.unregister();
		adminService = null;
		bundleContext = null;
	}
}
