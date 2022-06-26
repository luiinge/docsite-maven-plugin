package docsite;


import java.util.*;

public class SiteLanguage {

    public static List<SiteLanguage> of(String[] languageTags) {
        if (languageTags == null) return List.of();
        return of(Arrays.asList(languageTags));
    }

    public static List<SiteLanguage> of(List<String> languageTags) {
        if (languageTags == null || languageTags.isEmpty()) return List.of();
        List<SiteLanguage> list = new ArrayList<>(languageTags.size());
        list.add(new SiteLanguage(languageTags.get(0),true));
        for (int i = 1; i < languageTags.size(); i++) {
            list.add(new SiteLanguage(languageTags.get(i),false));
        }
        return list;
    }


    public static final SiteLanguage UNDEFINED = new SiteLanguage("",true);


    private final String language;
    private final String display;
    private final boolean primary;


    public SiteLanguage(String languageTag, boolean primary) {
        if (languageTag.contains(":")) {
            this.language = languageTag.split(":")[0];
            this.display = languageTag.split(":")[1];
        } else {
            this.language = languageTag;
            this.display = languageTag;
        }
        this.primary = primary;
    }



    public String language() {
        return this.language;
    }


    public String display() {
        return display;
    }


    public boolean isPrimary() {
        return this.primary;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteLanguage that = (SiteLanguage) o;
        return Objects.equals(language, that.language) && Objects.equals(display, that.display);
    }


    @Override
    public int hashCode() {
        return Objects.hash(language, display);
    }


}
