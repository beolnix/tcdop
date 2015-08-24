/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package io.cyberstock.tcdop.server.integration.teamcity.web;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.WebConstants;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beolnix on 20/06/15.
 */
public class TCDOPSettingsController extends BaseFormXmlController {

    // Dependencies
    private final SBuildServer server;
    private final PluginDescriptor pluginDescriptor;
    private final WebControllerManager manager;

    // Constants
    private static final Logger LOG = Logger.getInstance(TCDOPSettingsController.class.getName());
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
        webConfig.put(WebConstants.REFRESHABLE_PATH, myHtmlPath);
        webConfig.put(WebConstants.STYLES_PATH, myStylesPath);

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
