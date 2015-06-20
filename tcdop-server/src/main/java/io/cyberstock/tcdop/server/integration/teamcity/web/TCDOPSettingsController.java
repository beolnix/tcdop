package io.cyberstock.tcdop.server.integration.teamcity.web;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.server.service.TCDOPLoggerService;
import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.controllers.BasePropertiesBean;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import jetbrains.buildServer.controllers.admin.projects.PluginPropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.cyberstock.tcdop.server.service.ConfigurationValidator.validateConfiguration;

/**
 * Created by beolnix on 20/06/15.
 */
public class TCDOPSettingsController extends BaseFormXmlController {

    // Dependencies
    private final SBuildServer server;
    private final PluginDescriptor pluginDescriptor;
    private final WebControllerManager manager;

    // Constants
    private static final Logger LOG = Logger.getInstance(TCDOPLoggerService.LOGGER_NAME);
    private static final String PAGE_NAME = "do-profile-settings";
    public static final String HTML_PAGE_NAME = PAGE_NAME + ".html";
    public static final String JSP_PAGE_NAME = PAGE_NAME + ".jsp";
    public static final String STYLESHEET_NAME = PAGE_NAME + ".css";

    // State
    private final String myHtmlPath;
    private final String myJspPath;
    private final String myStylesPath;

    public TCDOPSettingsController(@NotNull final SBuildServer server,
                                   @NotNull final PluginDescriptor pluginDescriptor,
                                   @NotNull final WebControllerManager manager) {
        super(server);
        this.server = server;
        this.pluginDescriptor = pluginDescriptor;
        this.manager = manager;


        myHtmlPath = pluginDescriptor.getPluginResourcesPath(HTML_PAGE_NAME);
        myJspPath = pluginDescriptor.getPluginResourcesPath(JSP_PAGE_NAME);
        myStylesPath = pluginDescriptor.getPluginResourcesPath(STYLESHEET_NAME);

        manager.registerController(myHtmlPath, this);
    }

    @Override
    protected ModelAndView doGet(@NotNull HttpServletRequest request,
                                 @NotNull HttpServletResponse response) {
        Map<String, String> webConfig = new HashMap<String, String>();
        webConfig.put(DOConfigConstants.REFRESHABLE_PATH, myHtmlPath);
        webConfig.put(DOConfigConstants.STYLES_PATH, myStylesPath);

        final ModelAndView mv = new ModelAndView(myJspPath);
        mv.getModel().put("webConfig", webConfig);
        return mv;
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest request,
                          @NotNull HttpServletResponse response,
                          @NotNull Element xmlResponse) {
        //nop
    }
}
