package io.mapwize.mapwize;

import java.util.List;

public interface MWZSearchable {

    public String getName();
    public String getAlias();
    public String getIdentifier();
    public List<MWZTranslation> getTranslations();

}
