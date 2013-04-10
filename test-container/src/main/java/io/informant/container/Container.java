/**
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.informant.container;

import io.informant.container.config.ConfigService;
import io.informant.container.trace.TraceService;

/**
 * @author Trask Stalnaker
 * @since 0.5
 */
public interface Container {

    ConfigService getConfigService();

    void addExpectedLogMessage(String loggerName, String partialMessage) throws Exception;

    void executeAppUnderTest(Class<? extends AppUnderTest> appUnderTestClass) throws Exception;

    void interruptAppUnderTest() throws Exception;

    TraceService getTraceService();

    // checks no unexpected log messages
    // checks no active traces
    // resets Informant back to square one
    void checkAndReset() throws Exception;

    void close() throws Exception;

    @SuppressWarnings("serial")
    public static class StartupFailedException extends Exception {}
}