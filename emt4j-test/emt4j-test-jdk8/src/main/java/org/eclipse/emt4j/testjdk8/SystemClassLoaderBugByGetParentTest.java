/********************************************************************************
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.emt4j.testjdk8;

import org.eclipse.emt4j.common.JsonReport;
import org.eclipse.emt4j.test.common.SITBaseCase;
import org.eclipse.emt4j.test.common.TestConf;

import java.net.URLClassLoader;

@TestConf(mode = TestConf.ModeEnum.AGENT, from = TestConf.RELEASE.JDK8, to = TestConf.RELEASE.JDK11)
public class SystemClassLoaderBugByGetParentTest extends SITBaseCase {

    public void run() {
        MyClassLoader myClassLoader = new MyClassLoader(SystemClassLoaderBugByGetParentTest.class.getClassLoader());
        ClassLoader parent = myClassLoader.getParent();
        while (parent != null) {
            URLClassLoader urlClassLoader = (URLClassLoader) parent;
            parent = urlClassLoader.getParent();
        }
    }

    @Override
    public void verify(JsonReport jsonReport) {
        assertTrue(jsonReport.getResultDetailList().stream().filter((d) -> d.getMainResultCode().equals("SYSTEM_CLASSLOADER_TO_URLCLASSLOADER"))
                .findAny().isPresent(), "Expect found SYSTEM_CLASSLOADER_TO_URLCLASSLOADER,but actually not found");
    }
}
