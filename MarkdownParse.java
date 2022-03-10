// File reading code from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;



public class MarkdownParse {
        public static int indexOfUnescaped(String str, String search, int startIndex) {
        int currIndex = startIndex - 1;
        int backslashCount;
        do {
            currIndex = str.indexOf(search, currIndex + 1);
            backslashCount = 0;
            int index = currIndex - 1;
            while (index >= 0 && str.charAt(index) == '\\') {
                index--;
                backslashCount++;
            }
        } while (backslashCount % 2 == 1);
        return currIndex;
    }

 public static ArrayList<String> getLinksFromLine(String markdown) {
        ArrayList<String> toReturn = new ArrayList<>();
        // find the next [, then find the ], then find the (, then take up to
        // the next )
        // newline
        int currentIndex = 0;
        while (currentIndex < markdown.length()) {
            int nextOpenBracket = indexOfUnescaped(markdown, "[", currentIndex);
            if (nextOpenBracket == -1) {
                break;
            }
            int nextCloseBracket = indexOfUnescaped(markdown, "]", nextOpenBracket);
            int openParen = indexOfUnescaped(markdown, "(", nextCloseBracket);
            if (openParen == -1) {
                break;
            }
            int closeParen = indexOfUnescaped(markdown, ")", openParen);
            int nextOpenParen = indexOfUnescaped(markdown, "(", openParen + 1);
            if (nextOpenParen != -1 && closeParen > nextOpenParen) {
                // Invalid link because there's an open paren before the close paren
                int openBracket = indexOfUnescaped(markdown, "[", openParen);
                if (openBracket != -1) {
                    // Maybe there's another link
                    currentIndex = openBracket;
                    continue;
                } else {
                    break;
                }
            }
            if (closeParen == -1) {
                break;
            }
            // Check that this isn't an image link
            if (!(nextOpenBracket > 0 && markdown.charAt(nextOpenBracket - 1) == '!')) {
                String substr = markdown.substring(openParen + 1, closeParen);
                substr = substr.replaceAll("\\\\(.)", "$1");
                toReturn.add(substr);
            }
            currentIndex = closeParen + 1;
        }
        return toReturn;
    }

    public static ArrayList<String> getLinks(String markdown) {
        ArrayList<String> toReturn = new ArrayList<>();
        for (String line : markdown.split("\n")) {
            toReturn.addAll(getLinksFromLine(line));
        }
        return toReturn;
    }

        public static Map<String, List<String>> getLinks(File dirOrFile) throws IOException {
        Map<String, List<String>> result = new HashMap<>();
        if(dirOrFile.isDirectory()) {
            for(File f: dirOrFile.listFiles()) {
                result.putAll(getLinks(f));
            }
            return result;
        }
        else {
            Path p = dirOrFile.toPath();
            int lastDot = p.toString().lastIndexOf(".");
            if(lastDot == -1 || !p.toString().substring(lastDot).equals(".md")) {
                return result;
            }
            ArrayList<String> links = getLinks(Files.readString(p));
            result.put(dirOrFile.getPath(), links);
            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        File fileName1 = new File(args[0]);
        Map<String, List<String>> maplinks = getLinks(fileName1);
        System.out.println(maplinks);
    }
}
