package org.key_project.sed.key.evaluation.wizard.dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.key_project.sed.key.evaluation.model.definition.AbstractPage;
import org.key_project.sed.key.evaluation.model.definition.QuestionPage;
import org.key_project.sed.key.evaluation.model.input.AbstractFormInput;
import org.key_project.sed.key.evaluation.model.input.AbstractPageInput;
import org.key_project.sed.key.evaluation.model.input.EvaluationInput;
import org.key_project.sed.key.evaluation.model.input.SendFormPageInput;
import org.key_project.sed.key.evaluation.model.tooling.IWorkbenchModifier;
import org.key_project.sed.key.evaluation.util.LogUtil;
import org.key_project.sed.key.evaluation.wizard.EvaluationWizard;
import org.key_project.sed.key.evaluation.wizard.page.AbstractEvaluationWizardPage;
import org.key_project.sed.key.evaluation.wizard.page.SendFormWizardPage;
import org.key_project.util.java.ObjectUtil;

public class EvaluationWizardDialog extends WizardDialog {
   private static final Map<EvaluationInput, WeakHashMap<EvaluationWizardDialog, Void>> dialogInstances = new HashMap<EvaluationInput, WeakHashMap<EvaluationWizardDialog, Void>>();

   private final EvaluationInput evaluationInput;
   
   private final PropertyChangeListener currentPageListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         handleCurrentPageChanged(evt);
      }
   };

   private final PropertyChangeListener sendingListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         handleSendingInProgressChanged(evt);
      }
   };
   
   private boolean messageClickable = false;
   
   private Cursor handCursor;
   
   private final MouseListener messageMouseListener = new MouseListener() {
      @Override
      public void mouseUp(MouseEvent e) {
         handleMessageClick(e);
      }
      
      @Override
      public void mouseDown(MouseEvent e) {
      }
      
      @Override
      public void mouseDoubleClick(MouseEvent e) {
         handleMessageClick(e);
      }
   };

   public EvaluationWizardDialog(Shell parentShell, EvaluationInput evaluationInput) {
      super(parentShell, new EvaluationWizard(evaluationInput));
      this.evaluationInput = evaluationInput;
      evaluationInput.getCurrentFormInput().addPropertyChangeListener(AbstractFormInput.PROP_CURRENT_PAGE_INPUT, currentPageListener);
      setHelpAvailable(false);
   }

   public EvaluationInput getEvaluationInput() {
      return evaluationInput;
   }

   @Override
   protected int getShellStyle() {
      return SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.RESIZE | getDefaultOrientation();
   }

   @Override
   public void showPage(IWizardPage page) {
      if (getCurrentPage() instanceof SendFormWizardPage) {
         ((SendFormWizardPage) getCurrentPage()).getPageInput().removePropertyChangeListener(SendFormPageInput.PROP_SENDING_IN_PROGRESS, sendingListener);
      }
      getWizard().setCurrentPage(page);
      super.showPage(page);
      assert page instanceof AbstractEvaluationWizardPage<?>;
      AbstractEvaluationWizardPage<?> evaluationPage = (AbstractEvaluationWizardPage<?>) page;
      evaluationInput.getCurrentFormInput().setCurrentPageInput(evaluationPage.getPageInput());
      if (page instanceof SendFormWizardPage) {
         ((SendFormWizardPage) page).getPageInput().addPropertyChangeListener(SendFormPageInput.PROP_SENDING_IN_PROGRESS, sendingListener);
      }
   }
   
   @Override
   protected void nextPressed() {
      if (getWizard().nextPressed(getCurrentPage())) {
         super.nextPressed();
      }
   }

   @Override
   protected void finishPressed() {
      super.finishPressed();
      if (getReturnCode() == OK) {
         removeListener();
      }
   }

   @Override
   public AbstractEvaluationWizardPage<?> getCurrentPage() {
      return (AbstractEvaluationWizardPage<?>) super.getCurrentPage();
   }

   protected void handleCurrentPageChanged(PropertyChangeEvent evt) {
      if (evt.getNewValue() != getCurrentPage().getPageInput()) {
         IWizardPage newPage = getWizard().getPage((AbstractPageInput<?>)evt.getNewValue());
         assert newPage != null;
         showPage(newPage);
      }
   }

   protected void handleSendingInProgressChanged(PropertyChangeEvent evt) {
      boolean sendingInProgress = (Boolean)evt.getNewValue();
      if (sendingInProgress) {
         getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
         getButton(IDialogConstants.FINISH_ID).setEnabled(false);
         getButton(IDialogConstants.NEXT_ID).setEnabled(false);
         getButton(IDialogConstants.BACK_ID).setEnabled(false);
      }
      else {
         getButton(IDialogConstants.CANCEL_ID).setEnabled(true);
         getButton(IDialogConstants.FINISH_ID).setEnabled(true);
         getButton(IDialogConstants.NEXT_ID).setEnabled(true);
         getButton(IDialogConstants.BACK_ID).setEnabled(true);
         updateButtons();
      }
   }

   @Override
   protected EvaluationWizard getWizard() {
      return (EvaluationWizard)super.getWizard();
   }

   @Override
   public void openTray(DialogTray tray) throws IllegalStateException,UnsupportedOperationException {
      registerDialog(this);
      super.openTray(tray);
   }

   @Override
   public void create() {
      super.create();
      // Ensure that a button is selected, otherwise radio buttons might change values
      if (!getButton(IDialogConstants.NEXT_ID).isEnabled()) {
         getButton(IDialogConstants.CANCEL_ID).forceFocus();
      }
      // Perform runnable of current page if available
      if (getWizard().getCurrentPageRunnable() != null) {
         getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
               getCurrentPage().perfomRunnables(null, getWizard().getCurrentPageRunnable());
               getWizard().setCurrentPageRunnable(null);
            }
         });
      }
   }
   
   @Override
   protected Control createContents(Composite parent) {
      handCursor = new Cursor(getShell().getDisplay(), SWT.CURSOR_HAND);
      return super.createContents(parent);
   }

   protected Rectangle getConstrainedShellBounds(Rectangle preferredSize) {
      Rectangle result = super.getConstrainedShellBounds(preferredSize);
      if (result.width > 600) {
         result.width = 600;
      }
      if (result.height > 700) {
         result.height = 700;
      }
      return result;
   }

   @Override
   public int open() {
      registerDialog(this);
      return super.open();
   }

   @Override
   public boolean close() {
      boolean closed = super.close();
      if (closed) {
         handCursor.dispose();
         unregisterDialog(this);
         removeListener();
         try {
            if (!hasDialogs(evaluationInput)) {
               AbstractPage currentPage = getCurrentPage().getPageInput().getPage();
               if (currentPage instanceof QuestionPage) {
                  IWorkbenchModifier modifier = ((QuestionPage) currentPage).getWorkbenchModifier();
                  if (modifier != null) {
                     modifier.cleanWorkbench();
                  }
               }
            }
         }
         catch (Exception e) {
            LogUtil.getLogger().logError(e);
         }
      }
      return closed;
   }
   
   protected void removeListener() {
      if (getCurrentPage() instanceof SendFormWizardPage) {
         ((SendFormWizardPage) getCurrentPage()).getPageInput().removePropertyChangeListener(SendFormPageInput.PROP_SENDING_IN_PROGRESS, sendingListener);
      }
      evaluationInput.getCurrentFormInput().removePropertyChangeListener(AbstractFormInput.PROP_CURRENT_PAGE_INPUT, currentPageListener);
   }

   public static void registerDialog(EvaluationWizardDialog dialog) {
      synchronized (dialogInstances) {
         WeakHashMap<EvaluationWizardDialog, Void> evaluationMap = dialogInstances.get(dialog.getEvaluationInput());
         if (evaluationMap == null) {
            evaluationMap = new WeakHashMap<EvaluationWizardDialog, Void>();
            dialogInstances.put(dialog.getEvaluationInput(), evaluationMap);
         }
         evaluationMap.put(dialog, null);
      }
   }

   public static void unregisterDialog(EvaluationWizardDialog dialog) {
      synchronized (dialogInstances) {
         WeakHashMap<EvaluationWizardDialog, Void> evaluationMap = dialogInstances.get(dialog.getEvaluationInput());
         if (evaluationMap == null) {
            evaluationMap = new WeakHashMap<EvaluationWizardDialog, Void>();
            dialogInstances.put(dialog.getEvaluationInput(), evaluationMap);
         }
         evaluationMap.remove(dialog);
      }
   }
   
   public static boolean hasDialogs(EvaluationInput evaluationInput) {
      synchronized (dialogInstances) {
         WeakHashMap<EvaluationWizardDialog, Void> evaluationMap = dialogInstances.get(evaluationInput);
         if (evaluationMap != null) {
            return !evaluationMap.isEmpty();
         }
         else {
            return false;
         }
      }
   }
   
   @Override
   public void updateMessage() {
      super.updateMessage();
      boolean newClickable = getCurrentPage() != null && getCurrentPage().isMessageClickable();
      if (newClickable != messageClickable) {
         Control messageLabel = getMessageLabel();
         Control messageImageLabel = getMessageImageLabel();
         if (newClickable) {
            messageLabel.setCursor(handCursor);
            messageLabel.setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_LINK_FOREGROUND));
            messageLabel.addMouseListener(messageMouseListener);
            messageImageLabel.setCursor(handCursor);
            messageImageLabel.addMouseListener(messageMouseListener);
         }
         else {
            messageLabel.setCursor(null);
            messageLabel.setForeground(null);
            messageLabel.removeMouseListener(messageMouseListener);
            messageImageLabel.setCursor(null);
            messageImageLabel.removeMouseListener(messageMouseListener);
         }
         messageClickable = newClickable;
      }
   }

   protected void handleMessageClick(MouseEvent e) {
      if (getCurrentPage() != null) {
         getCurrentPage().performMessageClick();
      }
   }

   protected Control getMessageLabel() {
      try {
         return (Control) ObjectUtil.get(this, "messageLabel");
      }
      catch (Exception e) {
         LogUtil.getLogger().logError(e);
         return null;
      }
   }
   
   protected Control getMessageImageLabel() {
      try {
         return (Control) ObjectUtil.get(this, "messageImageLabel");
      }
      catch (Exception e) {
         LogUtil.getLogger().logError(e);
         return null;
      }
   }
}