package app.services;

 public class SvgTemplates {

     private StringBuilder svg;
     public SvgTemplates() {
         svg = new StringBuilder();
         svg.append(String.format(SVG_TEMPLATE, 1,1,"0 0 600 600"));

     }

     //canvas for the svg
    private static final String SVG_TEMPLATE = "<svg version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\" \n" +
             "xmlns=\"http://www.w3.org/2000/svg\" \n"+
            "     preserveAspectRatio=\"xMinYMin\"\n>";


     //white background rectangle
     private static final String SVG_BACKGROUND = "<rect x=\"50\" y=\"0\" width=\"%f\" height=\"%f\"\n" +
             "     stroke=\"gray\" fill=\"white\" stroke-width=\"0.6\"/>";



    //template for drawing posts, beams, rafters, sterns and roof
    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%f\" y=\"%f\" height=\"%f\" width=\"%f\" style=\"%s\" />";






    // the arrow line, horizontal
    private static final String SVG_HORIZONTAL_LINE_FOR_ARROW = "<line x1=\"180\" y1=\"50\" x2=\"%f\" y2=\"50\" style=\"stroke:#000000; stroke-width: 1px;\n" +
            "        marker-end: url(#endArrow);\" />\n";

     //the arrow line, vertical
     private static final String SVG_VERTICAL_LINE_FOR_ARROW = "<line x1=\"180\" y1=\"50\" x2=\"180\" y2=\"%f\" style=\"stroke:#000000; stroke-width: 1px;\n" +
             "        marker-end: url(#endArrow);\" />";






     //the arrow point, horizontal
     private static final String SVG_ARROWS_DEFS1 = "<defs>\n" +
             "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
             "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
             "        </marker>\n" +
             "    </defs>";

     //the arrow point, vertical
     private static final String SVG_ARROWS_DEFS2 = "<defs>\n" +
             "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
             "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
             "        </marker>\n" +
             "    </defs>";





    // text, horizontal
    private static final String SVG_HORIZONTAL_TEXT = "<text style=\"text-anchor: middle\" transform=\"translate(%f,40)\">%.2f cm</text>";

    // text, vertical
    private static final String SVG_VERTICAL_TEXT = "<text style=\"text-anchor: middle\" transform=\"translate(170,%f) rotate(-90)\">%.2f cm</text>";





    public void addBackground(double carportWidth, double carportLength) {
        svg.append(String.format(SVG_BACKGROUND, carportWidth, carportLength));
    }

    public void addRectangle(double x, double y, double length, double width, String style) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, length, width, style));
    }

    public void addHorizontalArrow(Double carportLength) {
        svg.append(String.format(SVG_HORIZONTAL_LINE_FOR_ARROW, carportLength));
        svg.append(SVG_ARROWS_DEFS1);
    }

     public void addHorizontalText(double carportLength, double carportLength2) {
         svg.append(String.format(SVG_HORIZONTAL_TEXT, carportLength, carportLength2));

     }

     public void addVerticalArrow(Double carportWidth) {
         svg.append(String.format(SVG_VERTICAL_LINE_FOR_ARROW, carportWidth));
         svg.append(SVG_ARROWS_DEFS2);
     }

    public void addVerticalText(double carportWidth, double carportWidth2) {
        svg.append(String.format(SVG_VERTICAL_TEXT,carportWidth, carportWidth2));
    }

    @Override
    public String toString() {
       return svg.append("</svg>").toString();
        //return svg.toString();
    }


     public String addN() {
         return svg.append("\n\"").toString();
     }
}
