/*
 * Copyright 2013-2016 the original author or authors.
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
package org.glowroot.agent.tests;

import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.glowroot.agent.it.harness.AppUnderTest;
import org.glowroot.agent.it.harness.Container;
import org.glowroot.agent.it.harness.Containers;
import org.glowroot.agent.it.harness.TransactionMarker;
import org.glowroot.agent.tests.app.MockDriver;
import org.glowroot.wire.api.model.TraceOuterClass.Trace;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcDriverIT {

    private static Container container;

    @BeforeClass
    public static void setUp() throws Exception {
        container = Containers.create();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        container.close();
    }

    @After
    public void afterEachTest() throws Exception {
        container.checkAndReset();
    }

    @Test
    public void shouldNotTriggerMockJdbcDriverToLoad() throws Exception {
        // when
        Trace trace = container.execute(ShouldGenerateTraceWithNestedEntries.class);

        // then
        Iterator<Trace.Entry> i = trace.getEntryList().iterator();

        Trace.Entry entry = i.next();
        assertThat(entry.getDepth()).isEqualTo(0);
        assertThat(entry.getMessage()).isEqualTo("major version");

        assertThat(i.hasNext()).isFalse();
    }

    public static class ShouldGenerateTraceWithNestedEntries
            implements AppUnderTest, TransactionMarker {
        @Override
        public void executeApp() throws Exception {
            transactionMarker();
        }
        @Override
        public void transactionMarker() throws Exception {
            new MockDriver().getMajorVersion();
        }
    }
}
