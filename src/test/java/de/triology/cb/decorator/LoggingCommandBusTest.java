/**
 * MIT License
 *
 * Copyright (c) 2017 TRIOLOGY GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.triology.cb.decorator;

import com.thekua.spikes.LogbackCapturingAppender;
import de.triology.cb.CommandBus;
import de.triology.cb.HelloCommand;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link LoggingCommandBus}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggingCommandBusTest {

  @Mock
  private CommandBus decorated;

  @InjectMocks
  private LoggingCommandBus loggingCommandBus;

  @After
  public void cleanUp() {
    LogbackCapturingAppender.cleanUpAll();
  }

  @Test
  public void execute() {
    LogbackCapturingAppender capturing = LogbackCapturingAppender.weaveInto(LoggingCommandBus.LOG);

    HelloCommand hello = new HelloCommand("joe");
    loggingCommandBus.execute(hello);
    verify(decorated).execute(hello);

    List<String> messages = capturing.getCapturedLogMessages();
    assertThat(messages.get(0)).contains("start").contains("HelloCommand");
    assertThat(messages.get(1)).contains("finish").contains("HelloCommand");
  }

}