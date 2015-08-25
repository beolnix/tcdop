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
package io.cyberstock.tcdop.server.integration.digitalocean.adapter;

import com.google.common.base.Optional;
import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.pojo.Account;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Key;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import jetbrains.buildServer.clouds.InstanceStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * The adapter for digital ocean client.
 *
 * Created by beolnix on 09/08/15.
 */
public interface DOAdapter {

    /**
     * Method sends a request to Digital Ocean to create a new droplet based on provided image
     * @param doSettings provided digital ocean client settings
     * @param cloudImage provided digital ocean image used to create droplet
     * @return Droplet
     * @throws DOError throws an error in case of any issue
     */
    @NotNull
    Droplet createInstance(DOSettings doSettings, DOCloudImage cloudImage) throws DOError;

    /**
     * Method sends a request to Diginal Ocean to create a new instance without an image
     * @param doSettings provided digital ocean client settings
     * @return Droplet
     * @throws DOError throws an error in case of any issue
     */
    Droplet createInstance(DOSettings doSettings) throws DOError;

    /**
     * Method sends request to Digital Ocean to verify is the provided token valid
     * @return Account if the token valid
     * @throws DOError throws an error in case of any issue
     */
    Account checkAccount() throws DOError;

    /**
     * Method blocks thread till the droplet is initialized successfully on a Digital Ocean side
     * @param dropletId dropletId used to check the state of the droplet on a Diginal Ocean side
     * @return ipv4 address as a String of successfully initialized droplet
     * @throws DOError throws an error in case of any issue
     */
    String waitForDropletInitialization(Integer dropletId) throws DOError;

    /**
     * Method sends droplet termination request to the Digital Ocean
     * @param instanceId droplet to be terminated
     * @return has the droplet been terminated successfully or not.
     * @throws DOError throws an error in case of any issue
     */
    Boolean terminateInstance(Integer instanceId) throws DOError;

    /**
     * Method sends droplet restart request to the Digital Ocean
     * @param instanceId doplet to be restarted
     * @return date and time of droplet start
     * @throws DOError throws an error in case of any issue
     */
    Date restartInstance(Integer instanceId) throws DOError;

    /**
     * Method finds image by name in user images on Digital Ocean
     * @param imageName the name of the image to be found
     * @return Optional of Image
     * @throws DOError throws an error in case of any issue
     */
    Optional<Image> findImageByName(String imageName) throws DOError;

    /**
     * Method returns image details from Digital Ocean by imageId
     * @param imageId image id to get the image
     * @return Optional of Image with all required details
     * @throws DOError throws an error in case of any issue
     */
    Optional<Image> getImageById(Integer imageId) throws DOError;

    /**
     * Transforms status from Digital Ocean format to the Teamcity Format
     * @param dropletStatus status to be converted
     * @return status in TeamCity format
     */
    InstanceStatus transformStatus(DropletStatus dropletStatus);

    /**
     * Requests available user images on Digital Ocean
     * @return List of Image
     * @throws DOError throws an error in case of any issue
     */
    List<Image> getImages() throws DOError;

    /**
     * Requests available user droplets on Digital Ocean
     * @return List of Droplet
     * @throws DOError throws an error in case of any issue
     */
    List<Droplet> getDroplets() throws DOError;

    /**
     * Creates a valid public key object which may be used in droplet creation request
     * @param name key name
     * @param publicKey public key in a format: key-type content comment
     * @return valid Key
     * @throws DOError throws an error in case of any issue
     */
    Key createSSHKey(String name, String publicKey) throws DOError;

}
