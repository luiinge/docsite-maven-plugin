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
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/prism.min.js")
            .attr(INTEGRITY, "sha512-YBk7HhgDZvBxmtOfUdvX0z8IH2d10Hp3aEygaMNhtF8fSOvBZ16D/1bXZTJV6ndk/L/DlXxYStP8jrF77v2MIg==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.java", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-java.min.js")
            .attr(INTEGRITY, "sha512-PWHY6Vao4E9K4LsGBYCY0ttDeiWZwuUozTbJvSy9UFHRz2J4Bl7rcWML3wEnJTVuCJhSwGne/8My5gTo/gnbpg==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.yaml", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-yaml.min.js")
            .attr(INTEGRITY, "sha512-QRKKJS95wG2dOCdc7Cm0Zbu+L04xY8fTwhHG3UnqZPMiFrAN8uXrqVTx//eqvTaoYwNJ7oFN3Vej5gnJ+GAxkw==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.json", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-json.min.js")
            .attr(INTEGRITY, "sha512-IC7rV8RslChgByOdUFC6ePqOGn+OwJhnKC3S5AezM8DAiOdGhJMwgsIvBChsa2yuxxoPbH2+W/kjNUM1cc+jUQ==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.css", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-css.min.js")
            .attr(INTEGRITY, "sha512-1qYok2x2Rsm2y+mrdyrp00iH7xYSgVyIQ1egDAoT7CBZ3kSzlaJK+NhWAh746NeL3gnH6dnP8FGS+3xOdwO7ig==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.javascript", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-javascript.min.js")
            .attr(INTEGRITY, "sha512-I4ZWqUpk7wqHcm7Gkv7k4IdgrDUTlGm1a7xeqyduqZLWeoGOn2E9us4XNBEDGclpk+6d1CmqIHYwmoyyL59zeA==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.bash", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-bash.min.js")
            .attr(INTEGRITY, "sha512-JvRd44DHaJAv/o3wxi/dxhz2TO/jwwX8V5/LTr3gj6QMQ6qNNGXk/psoingLDuc5yZmccOq7XhpVaelIZE4tsQ==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "prism.gherkin", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/components/prism-gherkin.min.js")
            .attr(INTEGRITY, "sha512-mIOZdB9UVqzUuNdWFcaqJssGJd6q7aaJpg+Q0z3kAWycvIrtcdiwELnKd2izg/ZlPqphj7rtqiUcmVS4bnd7RQ==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "katex", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.16.0/katex.min.js")
            .attr(INTEGRITY, "sha512-M7/jkZoKEln1jaaY2roCK9Jt4t+j/iru0e2vInDkVO5LY0EBt3m66tjTT5XFsGH2LJG+VRRL2ueIR3U0frs/GQ==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "mermaid", script()
            .attr(SRC,"https://cdnjs.cloudflare.com/ajax/libs/mermaid/9.1.2/mermaid.min.js")
            .attr(INTEGRITY, "sha512-8ZrL1m0+KXHylxkFJdFtFCauQdV/KZMjSBL9iydsyIfiB1LwYBlGegX8dUlLnUtRDrTgWKP6pPVyOqa2VrqoNA==")
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER)
    );



    private static final Map<String, LinkTag> links = Map.of(
        "prism.min", link()
            .attr(HREF,"https://cdnjs.cloudflare.com/ajax/libs/prism/1.23.0/themes/prism.min.css")
            .attr(INTEGRITY, "sha512-tN7Ec6zAFaVSG3TpNAKtk4DOHNpSwKHxxrsiw4GHKESGPs5njn/0sMCUMl2svV4wo4BK/rCP7juYz+zx+l6oeQ==")
            .attr(REL, STYLESHEET)
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "fontawesome.min", link()
            .attr(HREF,"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.0/css/all.min.css")
            .attr(INTEGRITY, "sha512-BnbUDfEUfV0Slx6TunuB042k9tuKe3xrD6q4mg5Ed72LTgzDIcLPxg6yI2gcMFRyomt+yJJxE+zJwNmxki6/RA==")
            .attr(REL, STYLESHEET)
            .attr(CROSSORIGIN, ANONYMOUS)
            .attr(REFERRERPOLICY, NO_REFERRER),
        "katex", link()
            .attr(HREF,"https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.16.0/katex.min.css")
            .attr(INTEGRITY, "sha512-Yfxo7zXGaQYyzWNxz8r4s8axNfG4jS3dips8p2HA/wNWmuapakkQiki+/XA3o3Ol+i8WI03cRJVDDUElEtED6g==")
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