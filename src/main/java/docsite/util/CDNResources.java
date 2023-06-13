package docsite.util;

import java.util.*;
import j2html.tags.specialized.*;
import static j2html.TagCreator.*;

public final class CDNResources {

    private CDNResources() { }


    private static final String INTEGRITY = "integrity";
    private static final String CROSSORIGIN = "crossorigin";
    private static final String ANONYMOUS = "anonymous";
    private static final String REFERRERPOLICY = "referrerpolicy";
    private static final String NO_REFERRER = "no-referrer";
    private static final String SRC = "src";
    private static final String STYLESHEET = "stylesheet";
    private static final String REL = "rel";
    private static final String HREF = "href";





    private static final Map<String, ScriptTag> scripts = Map.of(
        "prism.min", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js")
            .attr(INTEGRITY, "sha512-9khQRAUBYEJDCDVP2yw3LRUQvjJ0Pjx0EShmaQjcHa6AXiOv6qHQu9lCAIR8O+/D8FtaCoJ2c0Tf9Xo7hYH01Q==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.autoloader", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js")
            .attr(INTEGRITY, "sha512-SkmBfuA2hqjzEVpmnMt/LINrjop3GKWqsuLSSB3e7iBmYK7JuWw4ldmmxwD9mdm2IRTTi0OxSAfEGvgEi0i2Kw==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "katex", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.16.7/katex.min.js")
            .attr(INTEGRITY, "sha512-EKW5YvKU3hpyyOcN6jQnAxO/L8gts+YdYV6Yymtl8pk9YlYFtqJgihORuRoBXK8/cOIlappdU6Ms8KdK6yBCgA==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "mermaid", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/mermaid/10.2.3/mermaid.min.js")
            .attr(INTEGRITY, "sha512-M4zmdILgXOwrBEVfpVqdO/jC3BJZQzlXHeu967v41/QoT60sESWSLbi7YwntTbYC0ZjewfTbvXO8iKj7amvQow==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER)
    );



    private static final Map<String, LinkTag> links = Map.of(
        "prism.min", link()
            .attr(HREF,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css")
            .attr(INTEGRITY, "sha512-tN7Ec6zAFaVSG3TpNAKtk4DOHNpSwKHxxrsiw4GHKESGPs5njn/0sMCUMl2svV4wo4BK/rCP7juYz+zx+l6oeQ==")
            .attr(REL, STYLESHEET)
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "fontawesome.min", link()
            .attr(HREF,"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css")
            .attr(INTEGRITY, "sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==")
            .attr(REL, STYLESHEET)
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "katex", link()
            .attr(HREF,"https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.16.7/katex.min.css")
            .attr(INTEGRITY, "sha512-t2ALGTyUR6g1HJiHCmSTge2yGseGofdO88Q+zOWQx/N0ikecVw0YuyOet9xZDV8+Vx0Y0n1a3f3Qx3V9CcnsKA==")
            .attr(REL, STYLESHEET)
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER)
    );


    public static Optional<ScriptTag> js(String language) {
        return Optional.ofNullable(scripts.get(language));
    }

    public static Optional<LinkTag> css(String language) {
        return Optional.ofNullable(links.get(language));
    }

}