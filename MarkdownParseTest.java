import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MarkdownParseTest {


    public String readFile(String file) throws IOException {
        Path fileName = Path.of(file);
        String contents = Files.readString(fileName);

        return contents;
    }

    @Test
    public void testBaseCase() throws IOException {
        String contents = readFile("test-file.md");
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        ArrayList<String> Reallinks =
                new ArrayList<>(List.of("https://something.com", "some-page.html"));

        assertArrayEquals(links.toArray(), Reallinks.toArray());


    }

    @Test
    public void testEmptyCase() throws IOException {
        String contents = readFile("test-empty.md");
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        ArrayList<String> Reallinks = new ArrayList<>(List.of());

        assertArrayEquals(links.toArray(), Reallinks.toArray());

    }


    @Test
    public void testNewLine() throws IOException {
        String contents = readFile("test-file-newline.md");
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        ArrayList<String> Reallinks =
                new ArrayList<>(List.of("https://something.com", "some-page.html"));
        assertArrayEquals(links.toArray(), Reallinks.toArray());


    }

    @Test
    public void testBackSlash() throws IOException {
        String contents = readFile("test-backslash-escapes.md");
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        ArrayList<String> Reallinks = new ArrayList<>(List.of("/close_bracket", "/single_)bracket",
                "/double_\\", "/triple_\\)bracket", "/quad_\\\\", "/quad_\\\\\\", "/open_(paren"));
        assertArrayEquals(links.toArray(), Reallinks.toArray());

    }

    @Test
    public void testEndingText() throws IOException {
        String contents = readFile("test-ending-text.md");
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        ArrayList<String> Reallinks =
                new ArrayList<>(List.of("https://something.com", "some-page.html"));
        assertArrayEquals(links.toArray(), Reallinks.toArray());

    }

    @Test
    public void testIgnoreImage() throws IOException {
        String contents = readFile("test-ignore-image.md");
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        ArrayList<String> Reallinks =
                new ArrayList<>(List.of("/actual_link1", "/actual_link2", "/actual_link3"));
        assertArrayEquals(links.toArray(), Reallinks.toArray());

    }

    @Test
    public void testSnippet1() throws IOException {
        String contents = Files.readString(Path.of("./lab8_Snippet_1.md"));
        List<String> expect = List.of("`google.com", "google.com", "ucsd.edu");
        assertEquals(expect, MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSnippet2() throws IOException {
        String contents = Files.readString(Path.of("./lab8_Snippet_2.md"));
        List<String> expect = List.of("a.com", "a.com(())", "example.com");
        assertEquals(expect, MarkdownParse.getLinks(contents));
    }

    @Test
    public void testSnippet3() throws IOException {
        String contents = Files.readString(Path.of("./lab8_Snippet_3.md"));
        List<String> expect = List.of("https://ucsd-cse15l-w22.github.io/");
        assertEquals(expect, MarkdownParse.getLinks(contents));
    }
}
