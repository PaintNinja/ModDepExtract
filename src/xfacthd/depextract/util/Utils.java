package xfacthd.depextract.util;

import xfacthd.depextract.Main;

import java.io.*;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils
{
    public static String getForgeVersion(String forgeVersion)
    {
        if (forgeVersion.contains("-"))
        {
            forgeVersion = forgeVersion.substring(forgeVersion.indexOf("-") + 1);
        }
        return forgeVersion;
    }

    public static PrintWriter makePrintWriter(String fileName)
    {
        File outFile = new File(fileName);
        if (!outFile.exists())
        {
            try
            {
                //noinspection ResultOfMethodCallIgnored
                outFile.createNewFile();
            }
            catch (IOException e)
            {
                Main.LOG.error("Output file doesn't exist and file creation failed!");
                e.printStackTrace();
                return null;
            }
        }

        PrintWriter writer;
        try
        {
            writer = new PrintWriter(outFile);
        }
        catch (FileNotFoundException e)
        {
            Main.LOG.error("Can't open output file for writing!");
            e.printStackTrace();
            return null;
        }

        return writer;
    }

    public static <T> Predicate<T> customDistinct(Function<T, Object> keyExtractor)
    {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static String removePackage(String name)
    {
        int lastDot = name.lastIndexOf('.');
        if (lastDot != -1)
        {
            return name.substring(lastDot + 1);
        }
        return name;
    }

    public static String toFirstCharLower(String text)
    {
        return text.substring(0, 1).toLowerCase(Locale.ROOT) + text.substring(1);
    }

    public static String toLowerExceptFirst(String text)
    {
        return text.charAt(0) + text.substring(1).toLowerCase();
    }

    public static void openFileInDefaultSoftware(String fileName)
    {
        OS os = OS.get();
        if (os == null) { return; }

        try
        {
            String fileUrl = new File(fileName).toURI().toURL().toString();

            String command = switch (os)
            {
                case WINDOWS -> String.format("rundll32 url.dll,FileProtocolHandler \"%s\"", fileUrl);
                case LINUX -> String.format("xdg-open %s", fileUrl);
                case MAC -> String.format("open %s", fileUrl);
            };

            Runtime.getRuntime().exec(command);
        }
        catch (IOException e)
        {
            Main.LOG.error("Failed to open file in default app");
            e.printStackTrace();
        }
    }

    public enum OS
    {
        WINDOWS,
        LINUX,
        MAC;

        public static OS get()
        {
            String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);

            if (osName.contains("win")) { return WINDOWS; }
            if (osName.contains("linux") || osName.contains("unix")) { return LINUX; }
            if (osName.contains("mac")) { return MAC; }

            Main.LOG.error("Unknown operating system: %s", osName);
            return null;
        }
    }
}
