// File reading code from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


class LinkCounterVisitor extends AbstractVisitor {
    int linkCount = 0;
    ArrayList<String> links = new ArrayList<>();;

    @Override
    public void visit(Link link) {

        // This is called for all Link nodes. Override other visit methods for other node types.

        // Count links (this is just an example, don't actually do it this way for various
        // reasons).
        linkCount += 1;
        String nextLink = link.getDestination();
        links.add(nextLink);
        // Descend into children (could be omitted in this case because Text nodes don't have
        // children).
        visitChildren(link);
    }

}


public class MarkdownParse {

    public static ArrayList<String> getLinks(String markdown) {
        Parser parser = Parser.builder().build();

        Node document = parser.parse("This is *Sparta*");

        HtmlRenderer renderer = HtmlRenderer.builder().build();

        Node node = parser.parse(markdown);
        LinkCounterVisitor linkVisitor = new LinkCounterVisitor();
        node.accept(linkVisitor);
        return linkVisitor.links;

    }

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of(args[0]);
        String contents = Files.readString(fileName);
        ArrayList<String> links = getLinks(contents);
        System.out.println(links);
    }
}
