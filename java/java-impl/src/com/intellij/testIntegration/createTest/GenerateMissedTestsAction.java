/*
 * Copyright 2000-2014 JetBrains s.r.o.
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
package com.intellij.testIntegration.createTest;

import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.TestFrameworks;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.ide.util.PsiClassListCellRenderer;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testIntegration.JavaTestFinder;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.testIntegration.TestFramework;
import com.intellij.testIntegration.TestIntegrationUtils;
import com.intellij.ui.components.JBList;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateMissedTestsAction extends PsiElementBaseIntentionAction {

  @NotNull
  public String getText() {
    return "Generate missed test methods";
  }

  @NotNull
  public String getFamilyName() {
    return getText();
  }

  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    if (Extensions.getExtensions(TestFramework.EXTENSION_NAME).length == 0) return false;

    final PsiElement parent = element.getParent();
    if (!(parent instanceof PsiMethod)) return false;

    final PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
    return psiClass != null && JavaTestFinder.hasTestsForClass(psiClass);
  }

  @Override
  public void invoke(final @NotNull Project project, final Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    final PsiClass srcClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

    if (srcClass == null) return;

    final List<PsiElement> testClasses = TestFinderHelper.findTestsForClass(srcClass);
    if (testClasses.size() == 1) {
      generateMissedTests((PsiClass)testClasses.get(0), srcClass);
      return;
    }

    final JBList list = new JBList(testClasses);
    list.setCellRenderer(new PsiClassListCellRenderer());
    JBPopupFactory.getInstance().createListPopupBuilder(list)
      .setItemChoosenCallback(new Runnable() {
      @Override
      public void run() {
        generateMissedTests((PsiClass)list.getSelectedValue(), srcClass);
      }
    })
      .setTitle("Choose Test")
      .createPopup().showInBestPositionFor(editor);
  }

  private static void generateMissedTests(final PsiClass testClass, PsiClass srcClass) {
    if (testClass != null) {
      final TestFramework framework = TestFrameworks.detectFramework(testClass);
      if (framework != null) {
        final Project project = testClass.getProject();
        final Editor editor = CodeInsightUtil.positionCursor(project, testClass.getContainingFile(), testClass.getLBrace());
        final MissedTestsDialog dialog = new MissedTestsDialog(project, srcClass, testClass, framework);
        dialog.show();
        if (dialog.isOK()) {
          WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
              JavaTestGenerator.addTestMethods(editor, testClass, framework, dialog.getSelectedMethods(), false, false);
            }
          });
        }
      }
    }
  }

  public boolean startInWriteAction() {
    return false;
  }
}