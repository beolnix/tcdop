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
package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.cyberstock.tcdop.ssh.SSHChannelWrapper;
import io.cyberstock.tcdop.ssh.SSHSessionWrapper;

import java.util.Properties;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHSessionWrapperImpl implements SSHSessionWrapper {

    private final Session session;

    public SSHSessionWrapperImpl(Session session) {
        this.session = session;
    }

    public void setConfig(Properties newconf) {
        this.session.setConfig(newconf);
    }

    public boolean isConnected() {
        return this.session.isConnected();
    }

    public void connect() throws JSchException {
        this.session.connect();
    }

    public void disconnect() {
        this.session.disconnect();
    }

    public SSHChannelWrapper openChannel(String type) throws JSchException {
        return new SSHChannelWrapperImpl(session.openChannel(type));
    }
}
