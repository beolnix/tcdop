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
package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance;

import java.util.List;

/**
 * Digital Ocean cloud service provides all required images and droplets
 * management functionality for the integration with TeamCity
 *
 * Created by beolnix on 01/08/15.
 */
public interface DOClientService {

    /**
     * Method returns available user images
     * @return list of images
     * @throws DOError throws an error in case of any issue
     */
    List<DOCloudImage> getImages() throws DOError;

    /**
     * Blocks the thread till the instance is initialized.
     * @param cloudInstance cloud instance scheduled for the start
     */
    void waitInstanceInitialization(DOCloudInstance cloudInstance);

    /**
     * Restarts Instance in Digital Ocean
     * @param cloudInstance cloud instance to be restarted
     */
    void restartInstance(DOCloudInstance cloudInstance);

    /**
     * Terminates Instance in Digital Ocean
     * @param cloudInstance cloud instance to be terminated
     */
    void terminateInstance(final DOCloudInstance cloudInstance);

    /**
     * Creates new instance in Digital Ocean
     * @param cloudImage image used for the instance creatation
     * @param doSettings integration parameters
     * @return newly created instance
     * @throws DOError throws an error in case of any issue
     */
    DOCloudInstance createInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError;

    /**
     * Validates provided token. The method doesn't throw any issue if the token is valid.
     * @throws DOError throws an error in case of any issue
     */
    void accountCheck() throws DOError;

    /**
     * Method finds image by name
     * @param imageName name of the image to be found
     * @return the image
     * @throws DOError throws an error in case of any issue
     */
    DOCloudImage findImageByName(String imageName) throws DOError;
}
