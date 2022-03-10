import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

// # this class can be defined anywhere in the file
class WordCountVisitor extends AbstractVisitor {
    int wordCount = 0;

    @Override
    public void visit(Text text) {
        // This is called for all Text nodes. Override other visit methods for other node types.

        // Count words (this is just an example, don't actually do it this way for various
        // reasons).
        wordCount += text.getLiteral().split("\\W+").length;

        // Descend into children (could be omitted in this case because Text nodes don't have
        // children).
        visitChildren(text);
    }
}


class LinkCounterVisitor extends AbstractVisitor {
    int linkCount = 0;

    @Override
    public void visit(Link link) {
        // This is called for all Link nodes. Override other visit methods for other node types.

        // Count links (this is just an example, don't actually do it this way for various
        // reasons).
        linkCount += 1;
        // Descend into children (could be omitted in this case because Text nodes don't have
        // children).
        visitChildren(link);
    }

}


class TryCommonMark {

    public static void main(String[] args) {

        Parser parser = Parser.builder().build();

        Node document = parser.parse("This is *Sparta*");

        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // renderer.render(document); // "<p>This is <em>Sparta</em></p>\n"
        // System.out.println(renderer.render(document));

        // this part actually does the computation
        Node node = parser.parse("Example\n=======\n\nSome more text");
        WordCountVisitor visitor = new WordCountVisitor();
        node.accept(visitor);
        System.out.println(visitor.wordCount); // 4

        Node node2 =
                parser.parse("[a link!](https://something.com) \n [another link!](some-page.html)");
        LinkCounterVisitor linkVisitor = new LinkCounterVisitor();
        node2.accept(linkVisitor);
        System.out.println(linkVisitor.linkCount); // 4

    }
}

