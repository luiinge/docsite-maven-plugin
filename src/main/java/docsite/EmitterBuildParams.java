package docsite;

import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Accessors(chain = true, fluent = true) @With
public class EmitterBuildParams {

    private Docsite site;
    private SectionEmitter rootEmitter;
    private Section section;
    private List<SectionEmitter> ancestorEmitters;
    private ImageResolver globalImages;
    private Logger logger;

}
