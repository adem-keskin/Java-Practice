/**
   @version 1.00 2002-07-10
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

/**
   A program to test formatted text fields
*/   
public class FormatTest 
{
   public static void main(String[] args)
   {  
      FormatTestFrame frame = new FormatTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.show();
   }
}

/**
   A frame with a collection of formatted bext fields and
   a button that displays the field values.
*/
class FormatTestFrame extends JFrame
{  
   public FormatTestFrame()
   {  
      setTitle("FormatTest");
      setSize(WIDTH, HEIGHT);

      Container contentPane = getContentPane();

      JPanel buttonPanel = new JPanel();
      okButton = new JButton("Ok");
      buttonPanel.add(okButton);
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      mainPanel = new JPanel();
      mainPanel.setLayout(new GridLayout(0, 3));
      contentPane.add(mainPanel, BorderLayout.CENTER);

      JFormattedTextField intField = new JFormattedTextField(
         NumberFormat.getIntegerInstance());
      intField.setValue(new Integer(100));
      addRow("Number:", intField);

      JFormattedTextField intField2 = new JFormattedTextField(
         NumberFormat.getIntegerInstance());
      intField2.setValue(new Integer(100));
      intField2.setFocusLostBehavior(JFormattedTextField.COMMIT);
      addRow("Number (Commit behavior):", intField2);

      JFormattedTextField intField3
         = new JFormattedTextField(new 
            InternationalFormatter(
               NumberFormat.getIntegerInstance())
            {
               protected DocumentFilter getDocumentFilter()
               {
                  return filter;
               }
               private DocumentFilter filter 
                  = new IntFilter();
            });
      intField3.setValue(new Integer(100));                     
      addRow("Filtered Number", intField3);

      JFormattedTextField intField4 = new JFormattedTextField(
         NumberFormat.getIntegerInstance());
      intField4.setValue(new Integer(100));
      intField4.setInputVerifier(
         new FormattedTextFieldVerifier());
      addRow("Verified Number:", intField4);

      JFormattedTextField currencyField = new 
         JFormattedTextField(NumberFormat.getCurrencyInstance());
      currencyField.setValue(new Double(10));
      addRow("Currency:", currencyField);

      JFormattedTextField dateField = new JFormattedTextField(
         DateFormat.getDateInstance());
      dateField.setValue(new Date());
      addRow("Date (default):", dateField);

      DateFormat format = 
         DateFormat.getDateInstance(DateFormat.SHORT);
      format.setLenient(false);
      JFormattedTextField dateField2 = new JFormattedTextField(
         format);
      dateField2.setValue(new Date());
      addRow("Date (short, not lenient):", dateField2);

      try
      {
         DefaultFormatter formatter = new DefaultFormatter();
         formatter.setOverwriteMode(false);
         JFormattedTextField urlField = new JFormattedTextField(
            formatter);
         urlField.setValue(new URL("http://java.sun.com"));
         addRow("URL:", urlField);
      }
      catch (MalformedURLException exception)
      {
         exception.printStackTrace();
      }

      try
      {
         MaskFormatter formatter 
            = new MaskFormatter("###-##-####");
         formatter.setPlaceholderCharacter('0');
         JFormattedTextField ssnField 
            = new JFormattedTextField(formatter);             
         ssnField.setValue("078-05-1120");
         addRow("SSN Mask:", ssnField);      
      }
      catch (ParseException exception)
      {
         exception.printStackTrace();
      }

      JFormattedTextField ipField 
         = new JFormattedTextField(new IPAddressFormatter());
      ipField.setValue(new byte[] { (byte)130, 65, 86, 66 });
      addRow("IP Address:", ipField);
   }

   /**
      Adds a row to the main panel.
      @param labelText the label of the field
      @param field the sample field
   */
   public void addRow(String labelText, 
      final JFormattedTextField field)
   {
      mainPanel.add(new JLabel(labelText));
      mainPanel.add(field);
      final JLabel valueLabel = new JLabel();
      mainPanel.add(valueLabel);
      okButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               Object value = field.getValue();
               if (value.getClass().isArray())
               { 
                  StringBuffer buffer = new StringBuffer();
                  buffer.append('{');
                  for (int i = 0; i < Array.getLength(value);
                       i++)
                  {
                     if (i > 0) buffer.append(',');
                     buffer.append(
                        Array.get(value, i).toString());
                  }
                  buffer.append('}');
                  valueLabel.setText(buffer.toString());
               }
               else
                  valueLabel.setText(value.toString());
            }
         });
   }
    
   public static final int WIDTH = 500;
   public static final int HEIGHT = 250;  

   private JButton okButton;
   private JPanel mainPanel;
}

/**
   A filter that restricts input to digits and a '-' sign.
*/
class IntFilter extends DocumentFilter
{
   public void insertString(FilterBypass fb, int offset,
      String string, AttributeSet attr) 
      throws BadLocationException 
   {
      StringBuffer buffer = new StringBuffer(string);
      for (int i = buffer.length() - 1; i >= 0; i--)
      {
         char ch = buffer.charAt(i);
         if (!Character.isDigit(ch) && ch != '-') 
            buffer.deleteCharAt(i);
      }
      super.insertString(fb, offset, buffer.toString(), attr);
   }

   public void replace(FilterBypass fb, int offset, 
      int length, String string, AttributeSet attr) 
      throws BadLocationException 
   {
      if (string != null) 
      {
         StringBuffer buffer = new StringBuffer(string);
         for (int i = buffer.length() - 1; i >= 0; i--)
         {
            char ch = buffer.charAt(i);
            if (!Character.isDigit(ch) && ch != '-') 
               buffer.deleteCharAt(i);
         }
         string = buffer.toString();
      }
      super.replace(fb, offset, length, string, attr);
   }
}

/**
   A verifier that checks whether the contents of 
   a formatted text field is valid.
*/
class FormattedTextFieldVerifier extends InputVerifier 
{
   public boolean verify(JComponent component) 
   {         
      JFormattedTextField field = (JFormattedTextField)component;
      return field.isEditValid();
   }     
}

/**
   A formatter for 4-byte IP addresses of the form a.b.c.d
*/
class IPAddressFormatter extends DefaultFormatter
{
   public String valueToString(Object value) 
      throws ParseException
   {
      if (!(value instanceof byte[])) 
         throw new ParseException("Not a byte[]", 0);
      byte[] a = (byte[])value;
      if (a.length != 4) 
         throw new ParseException("Length != 4", 0);
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < 4; i++)
      {
         int b = a[i];
         if (b < 0) b += 256;
         buffer.append(String.valueOf(b));
         if (i < 3) buffer.append('.');
      }
      return buffer.toString();
   }

   public Object stringToValue(String text) throws ParseException
   {
      StringTokenizer tokenizer = new StringTokenizer(text, ".");
      byte[] a = new byte[4];
      for (int i = 0; i < 4; i++)
      {
         int b = 0;
         try
         {
            b = Integer.parseInt(tokenizer.nextToken());
         }
         catch (NumberFormatException e)
         {
            throw new ParseException("Not an integer", 0);
         }
         if (b < 0 || b >= 256) 
            throw new ParseException("Byte out of range", 0);
         a[i] = (byte)b;
      }
      return a;
   }
}
