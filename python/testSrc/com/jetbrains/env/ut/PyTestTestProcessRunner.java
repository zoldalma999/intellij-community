/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.jetbrains.env.ut;

import com.jetbrains.env.ProcessWithConsoleRunner;
import com.jetbrains.python.testing.newTestRunners.PyPyTestConfiguration;
import com.jetbrains.python.testing.newTestRunners.PyPyTestFactory;
import org.jetbrains.annotations.NotNull;


/**
 * {@link ProcessWithConsoleRunner} to pytest
 *
 * @author Ilya.Kazakevich
 */
public class PyTestTestProcessRunner extends PyScriptTestProcessRunner<PyPyTestConfiguration> {
  public PyTestTestProcessRunner(@NotNull final String scriptName, final int timesToRerunFailedTests) {
    super(PyPyTestFactory.INSTANCE,
          PyPyTestConfiguration.class, scriptName, timesToRerunFailedTests);
  }
}
