package xfacthd.depextract.html;

import com.google.common.base.Preconditions;

import java.io.PrintWriter;

public class HtmlWriter
{
    private final PrintWriter writer;
    private int indent = 0;
    private boolean newLine = true;

    public HtmlWriter(PrintWriter writer) { this.writer = writer; }

    public void print(String line)
    {
        if (newLine)
        {
            printIndent();
            writer.println(line);
        }
        else
        {
            writer.print(line);
        }
    }

    public void printMultiLine(String text)
    {
        Preconditions.checkState(newLine, "Multiline print is only available when newlines are enabled");
        for (String line : text.split("\n"))
        {
            printIndent();
            writer.println(line);
        }
    }

    public void println(String line) { print(line + "<br>"); }

    public void printIndent()
    {
        for (int i = 0; i < indent * 4; i++)
        {
            writer.write(' ');
        }
    }

    public void push() { indent++; }

    public void pop() { indent = Math.max(indent - 1, 0); }

    public void enableNewLine() { newLine = true; }

    public void disableNewLine() { newLine = false; }
}
