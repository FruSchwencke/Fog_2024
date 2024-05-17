package app.services;

 public class SvgTemplates {

     private StringBuilder svg;
     public SvgTemplates() {
         svg = new StringBuilder();
         svg.append(String.format(SVG_TEMPLATE, 1,80,"0 0 1000 880","100%"));
     }

     //canvas for the svg
    private static final String SVG_TEMPLATE = "<svg version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\"  width=\"%s\" \n" +
             "xmlns=\"http://www.w3.org/2000/svg\" \n"+
            "     preserveAspectRatio=\"xMinYMin\">";


    //template for drawing posts, beams, rafters, sterns and roof
    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%f\" y=\"%f\" height=\"%f\" width=\"%f\" style=\"%s\" />";


    //the arrow point
    private static final String SVG_ARROWS_DEFS = "<defs>\n" +
            "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "    </defs>";


    // the arrow line
    private static final String SVG_LINES_FOR_ARROWS = "<line x1=\"50\" y1=\"600\" x2=\"50\" y2=\"50\" style=\"stroke:#000000;\n" +
            "        marker-end: url(#endArrow);\" />\n" +
            "\n" +
            "    <line x1=\"50\" y1=\"600\" x2=\"800\" y2=\"600\" style=\"stroke:#000000;\n" +
            "        marker-end: url(#endArrow);\" />";


    // text horizontal
    private static final String SVG_HORIZONTAL_TEXT = "<text style=\"text-anchor: middle\" transform=\"translate(%f,%f)\">%f</text>";

    // text vertical
    private static final String SVG_VERTICAL_TEXT = "<text style=\"text-anchor: middle\" transform=\"translate(%f,%f) rotate(-90)\">%f</text>";






    public void Svg(int x, int y, String viewBox, String width) {

        svg.append(String.format(SVG_TEMPLATE, x, y, viewBox, width));

    }

    public void addRectangle(double x, double y, double length, double width, String style) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, length, width, style));
    }

    public void addArrow(int x1, int y1, int x2, int y2, String style) {
        svg.append(String.format(SVG_LINES_FOR_ARROWS));
        svg.append(SVG_ARROWS_DEFS);
    }

    public void addText(int x, int y, int rotation, String text) {
        svg.append(SVG_HORIZONTAL_TEXT);
        svg.append(SVG_VERTICAL_TEXT);
    }

    public void addLine(int x1, int y1, int x2, int y2, String style) {

    }



    @Override
    public String toString() {
        return svg.append("</svg>").toString();
    }
}
