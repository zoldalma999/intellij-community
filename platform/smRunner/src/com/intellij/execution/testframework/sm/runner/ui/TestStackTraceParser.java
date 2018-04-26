// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.execution.testframework.sm.runner.ui;

import com.intellij.openapi.util.text.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestStackTraceParser {

  private final static Pattern outerPattern = Pattern.compile("\tat (.*)\\.([^.]*)\\((.*)\\)");
  private final static Pattern innerPattern = Pattern.compile("(.*):(\\d*)");

  private int myFailedLine = -1;
  private String myFailedMethodName;
  private String myErrorMessage;

  public int getFailedLine() {
    return myFailedLine;
  }

  public String getFailedMethodName() {
    return myFailedMethodName;
  }

  public String getErrorMessage() {
    return myErrorMessage;
  }

  public TestStackTraceParser(String url, String stacktrace, String errorMessage) {
    myErrorMessage = errorMessage;
    if (stacktrace == null || url == null) return;
    int i = url.indexOf("//");
    if (i == -1) return;
    String path = "\tat " + url.substring(i + 2);

    try (BufferedReader reader = new BufferedReader(new StringReader(stacktrace))) {
      String line, previous = null;
      while ((line = reader.readLine()) != null) {
        if (StringUtil.isEmpty(myErrorMessage)) {
          myErrorMessage = line;
        }
        if (line.startsWith(path)) {
          Matcher matcher = outerPattern.matcher(line);
          if (!matcher.matches()) return;
          Matcher matcher1 = innerPattern.matcher(matcher.group(3));
          if (!matcher1.matches()) return;
          myFailedLine = Integer.parseInt(matcher1.group(2));

          if (previous == null) return;
          Matcher matcher2 = outerPattern.matcher(previous);
          if (!matcher2.matches()) return;
          myFailedMethodName = matcher2.group(2);
        }
        previous = line;
      }

    }
    catch (IOException | NumberFormatException ignore) {
    }
  }
}
