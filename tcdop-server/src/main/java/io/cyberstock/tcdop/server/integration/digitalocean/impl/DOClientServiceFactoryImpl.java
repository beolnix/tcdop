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
package io.cyberstock.tcdop.server.integration.digitalocean.impl;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl;

/**
 * Created by beolnix on 27/06/15.
 */
public class DOClientServiceFactoryImpl implements DOClientServiceFactory {

    // state
    private Integer actionResultCheckInterval = 2 * 1000;
    private Long actionWaitThreshold = 20 * 60 * 1000L;

    public DOClientServiceFactoryImpl() {

    }

    public DOClientService createClient(String token) {
        DigitalOcean doClient = new DigitalOceanClient(token);
        DOAdapter doAdapter = new DOAdapterImpl(doClient, actionResultCheckInterval, actionWaitThreshold);
        DOClientServiceImpl clientService = new DOClientServiceImpl(doAdapter);
        return clientService;
    }

    public void setActionResultCheckInterval(Integer actionResultCheckInterval) {
        this.actionResultCheckInterval = actionResultCheckInterval;
    }

    public void setActionWaitThreshold(Long actionWaitThreshold) {
        this.actionWaitThreshold = actionWaitThreshold;
    }
}
