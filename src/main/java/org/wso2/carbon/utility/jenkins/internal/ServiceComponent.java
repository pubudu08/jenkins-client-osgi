package org.wso2.carbon.utility.jenkins.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.utility.continuousintegration.IContinuousIntegration;
import org.wso2.carbon.utility.jenkins.service.CIAdminService;


/**
 * @author Pubudu Dissanayake : pubudud@wso2.com on 2/7/14.
 * @scr.component name="org.wso2.carbon.utility.jenkins"
 * immediate="true"
 */
public class ServiceComponent {
    private ServiceRegistration registration;

    private static CIAdminService adminService;
    private static BundleContext bundleContext;
    private static final Log logger = LogFactory.getLog(ServiceComponent.class);

    protected void activate(ComponentContext context) {
       logger.info("Continuous Integration Service: Jenkins bundle is activated");
        adminService = new CIAdminService();
        bundleContext = context.getBundleContext();
        registration = bundleContext.registerService(IContinuousIntegration.class.getName(),adminService, null);
    }

    protected void deactivate(ComponentContext context) {
        logger.info("Continuous Integration Service: Jenkins bundle is deactivated");
        registration.unregister();
        adminService = null;
        bundleContext = null;
    }
}
