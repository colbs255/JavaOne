package org.j19lab;

public class TextBlocks {
    public static void main(String[] args) {
        String html = "<html>\n" +
                "    <body>\n" +
                "        <p>Hello, world</p>\n" +
                "    </body>\n" +
                "</html>\n";
        String better = """
            <html>
            <body>
                <p>Hello, world</p>
            </body>
            </html>
            """;
        // the resulting string has a single line
        String text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing \
        elit, sed do eiusmod tempor incididunt ut labore \
        et dolore magna aliqua.\
        """;
        System.out.println(better);

    }
}
