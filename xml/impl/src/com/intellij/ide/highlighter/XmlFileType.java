/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.intellij.ide.highlighter;

import com.intellij.codeInsight.editorActions.TypedHandler;
import com.intellij.ide.IdeBundle;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class XmlFileType extends XmlLikeFileType implements DomSupportEnabled {
  public static final XmlFileType INSTANCE = new XmlFileType();
  @NonNls public static final String DEFAULT_EXTENSION = "xml";
  @NonNls public static final String DOT_DEFAULT_EXTENSION = "."+DEFAULT_EXTENSION;
  private static final Icon ICON = IconLoader.getIcon("/fileTypes/xml.png");

  private XmlFileType() {
    super(XMLLanguage.INSTANCE);
    TypedHandler.registerBaseLanguageQuoteHandler(XMLLanguage.class, TypedHandler.getQuoteHandlerForType(this));
  }

  @NotNull
  public String getName() {
    return "XML";
  }

  @NotNull
  public String getDescription() {
    return IdeBundle.message("filetype.description.xml");
  }

  @NotNull
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  public Icon getIcon() {
    return ICON;
  }
}