package tutothr.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class PageUrlLinkBuilder {
    
    /**
     * Erstellt eine URL basierend auf dem aktuellen Request, wobei der "page"-Parameter
     * ausgetauscht wird. Alle anderen Parameter (Filter, Sortierung) bleiben erhalten.
     */
    public String replacePageParam(int page) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("page", page)
                .build()
                .toUriString();
    }
}
