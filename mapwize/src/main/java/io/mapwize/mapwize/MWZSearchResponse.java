package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Wrap a list of suggestion base on algolia response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZSearchResponse {

    private int nbHits;
    private int page;
    private int nbPages;
    private int hitsPerPage;
    private List<Map<String,Object>> hits;

    public MWZSearchResponse() {
        super();
    }

    public List<Map<String,Object>> getHits() {
        return hits;
    }

    public void setHits(List<Map<String,Object>> hits) {
        this.hits = hits;
    }

    public int getNbHits() {
        return nbHits;
    }

    public void setNbHits(int nbHits) {
        this.nbHits = nbHits;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public void setHitsPerPage(int hitsPerPage) {
        this.hitsPerPage = hitsPerPage;
    }

}