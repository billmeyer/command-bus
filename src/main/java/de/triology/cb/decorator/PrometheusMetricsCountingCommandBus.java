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

import de.triology.cb.Command;
import de.triology.cb.CommandBus;
import io.prometheus.client.Counter;

/**
 * Command bus decorator counting the executed commands using a Prometheus {@link Counter}
 */
public class PrometheusMetricsCountingCommandBus implements CommandBus {

  private CommandBus decorated;
  private Counter counter;

  /**
   * Creates a new PrometheusMetricsCountingCommandBus
   *
   * @param decorated command bus to decorate
   * @param counter counter to increment on executing commands
   */
  public PrometheusMetricsCountingCommandBus(CommandBus decorated, Counter counter) {
    this.decorated = decorated;
    this.counter = counter;
  }

  /**
   * Delegates the provided command to the decorated command bus and increases the given counter using the command's
   * classname as a label
   *
   * @param command command object
   * @param <R> type of return value
   * @param <C> type of command
   */
  @Override
  public <R, C extends Command<R>> R execute(C command) {
    R result = decorated.execute(command);
    counter.labels(command.getClass().getSimpleName()).inc();
    return result;
  }

}
