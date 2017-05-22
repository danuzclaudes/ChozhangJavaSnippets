package playground;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A very simple version of the 'grep' program.
 * {args: RegexJGrep.java "\\b[Ssctp]\\w+"}
 */
public class RegexJGrep {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java RegexJGrep filename regex");
            System.exit(0);
        }
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        Matcher m = Pattern.compile(args[1]).matcher("");
        // Iterate through the lines of input file
        int index = 0;
        String line;
        while ((line = br.readLine()) != null ) {
            m.reset(line);
            while(m.find()) {
                System.out.println(index++ + ": " + m.group() + ": " + m.start());
            }
        }
    }
}
